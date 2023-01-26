package com.star.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.constant.RabbitmqConst;
import com.star.common.exception.StarryException;
import com.star.common.tool.BeanCopyUtil;
import com.star.common.tool.PageUtils;
import com.star.common.tool.RedisUtils;
import com.star.inf.dto.ArticleBackDTO;
import com.star.inf.dto.PageData;
import com.star.inf.entity.Article;
import com.star.inf.entity.ArticleTag;
import com.star.inf.entity.Category;
import com.star.inf.entity.Tag;
import com.star.inf.mapper.ArticleMapper;
import com.star.inf.mapper.ArticleTagMapper;
import com.star.inf.mapper.CategoryMapper;
import com.star.inf.mapper.TagMapper;
import com.star.inf.search.ArticleMqMessage;
import com.star.inf.service.ArticleTagServiceImpl;
import com.star.inf.service.TagServiceImpl;
import com.star.inf.utils.UserUtils;
import com.star.inf.vo.ArticleVO;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.DeleteVO;
import com.star.inf.vo.TopVO;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.FALSE;
import static com.star.common.constant.RedisConst.ARTICLE_LIKE_COUNT;
import static com.star.common.constant.RedisConst.ARTICLE_VIEWS_COUNT;
import static com.star.common.enums.StatusEnum.DRAFT;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@Service
public class AdminArticleService extends ServiceImpl<ArticleMapper, Article> {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ArticleTagMapper articleTagMapper;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private AmqpTemplate amqpTemplate;

    @Resource
    private TagServiceImpl tagServiceImpl;

    @Resource
    private ArticleTagServiceImpl articleTagServiceImpl;

    /**
     * 查询后台文章
     *
     * @param condition 条件
     * @return 文章列表
     */
    public PageData<ArticleBackDTO> listArticleBack(ConditionVO condition) {
        // 查询文章总量
        Long count = articleMapper.countArticlesBack(condition);
        if (count == 0) {
            return new PageData<>();
        }
        // 查询后台文章
        List<ArticleBackDTO> articleBackList = articleMapper.listArticlesBack(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        // 查询文章点赞量和浏览量
        Map<String, Object> likeCountMap = redisUtils.hGetAll(ARTICLE_LIKE_COUNT);
        Map<Object, Double> viewsCountMap = redisUtils.zAllScore(ARTICLE_VIEWS_COUNT);
        // 封装点赞量和浏览量
        articleBackList.forEach(v -> {
            v.setLikeCount((Integer) likeCountMap.get(v.getId().toString()));
            Double viewCount = viewsCountMap.get(v.getId());
            if (Objects.nonNull(viewCount)) {
                v.setViewsCount(viewCount.intValue());
            }
        });
        return new PageData<>(articleBackList, count);
    }

    /**
     * 添加或修改文章
     *
     * @param articleVO 文章对象
     */
    @Transactional(rollbackFor = StarryException.class)
    public void saveOrUpdateArticle(ArticleVO articleVO) {
        // 保存文章分类
        Category category = saveArticleCategory(articleVO);
        // 保存或修改文章
        Article article = BeanCopyUtil.copyObject(articleVO, Article.class);
        if (Objects.nonNull(category)) {
            article.setCategoryId(category.getId());
        }
        article.setUserId(UserUtils.getUserInfoId());
        this.saveOrUpdate(article);
        // 保存文章标签
        saveArticleTag(articleVO, article.getId());
        // es
        amqpTemplate.convertAndSend(RabbitmqConst.ES_CHANGE,
                RabbitmqConst.ES_BIND_KEY, new ArticleMqMessage(article.getId(), ArticleMqMessage.CREATE_OR_UPDATE));
    }

    /**
     * 保存文章标签
     *
     * @param articleVO 文章信息
     * @param articleId 文章Id
     */
    @Transactional(rollbackFor = StarryException.class)
    public void saveArticleTag(ArticleVO articleVO, Integer articleId) {
        // 编辑文章则删除文章所有标签
        if (Objects.nonNull(articleVO.getId())) {
            articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                    .eq(ArticleTag::getArticleId, articleVO.getId()));
        }
        // 添加文章标签
        List<String> tagNameList = articleVO.getTagNameList();
        if (CollectionUtils.isEmpty(tagNameList)) {
            return;
        }
        // 查询已存在的标签
        List<Tag> existTagList = tagServiceImpl.list(new LambdaQueryWrapper<Tag>().in(Tag::getTagName, tagNameList));
        List<String> existTagNameList = existTagList.stream().map(Tag::getTagName).collect(Collectors.toList());
        List<Integer> existTagIdList = existTagList.stream().map(Tag::getId).collect(Collectors.toList());

        // 对比新增不存在的标签
        tagNameList.removeAll(existTagNameList);
        // 剩下的为新增的
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            List<Tag> tagList = tagNameList.stream().map(v ->
                            Tag.builder().tagName(v).build())
                    .collect(Collectors.toList());
            tagServiceImpl.saveBatch(tagList);
            List<Integer> tagIdList = tagList.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());
            existTagIdList.addAll(tagIdList);
        }
        // 关联表维护
        List<ArticleTag> articleTagList = existTagIdList.stream().map(v ->
                        ArticleTag.builder().articleId(articleId).tagId(v).build())
                .collect(Collectors.toList());
        articleTagServiceImpl.saveBatch(articleTagList);
    }

    /**
     * 保存文章分类
     *
     * @param articleVO 文章信息
     * @return {@link Category} 文章分类
     */
    @Transactional(rollbackFor = StarryException.class)
    public Category saveArticleCategory(ArticleVO articleVO) {
        // 首先判断分类是否存在
        Category category = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                .eq(Category::getCategoryName, articleVO.getCategoryName()));
        if (Objects.isNull(category) &&
                !articleVO.getStatus().equals(DRAFT.getStatus())) {
            // 不存在新增
            category = Category.builder().categoryName(articleVO.getCategoryName()).build();
            categoryMapper.insert(category);
        }
        return category;
    }

    /**
     * 修改文章置顶状态
     *
     * @param articleTopVO
     */
    @Transactional(rollbackFor = StarryException.class)
    public void updateArticleTop(TopVO articleTopVO) {
        Article article = Article.builder()
                .id(articleTopVO.getId())
                .isTop(articleTopVO.getIsTop()).build();
        articleMapper.updateById(article);
    }

    /**
     * 修改文章逻辑删除状态
     *
     * @param deleteVO
     */
    @Transactional(rollbackFor = StarryException.class)
    public void updateArticleDelete(DeleteVO deleteVO) {
        List<Article> articleList = deleteVO.getIdList().stream().map(id ->
                Article.builder()
                        .id(id)
                        .isTop(FALSE)
                        .isDelete(deleteVO.getIsDelete()).build()).collect(Collectors.toList());
        this.updateBatchById(articleList);
    }

    /**
     * 删除文章标签关联
     *
     * @param articleIdList
     */
    @Transactional(rollbackFor = StarryException.class)
    public void deleteArticles(List<Integer> articleIdList) {
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, articleIdList));
        articleMapper.deleteBatchIds(articleIdList);

        // es
        amqpTemplate.convertAndSend(RabbitmqConst.ES_CHANGE,
                RabbitmqConst.ES_BIND_KEY, new ArticleMqMessage(articleIdList.get(0), ArticleMqMessage.REMOVE));
    }

    /**
     * 根据id查看后台文章
     *
     * @param articleId 文章id
     * @return 文章
     */
    public ArticleVO getArticleBackById(Integer articleId) {
        // 查询文章信息
        Article article = articleMapper.selectById(articleId);
        // 查询分类信息
        Category category = categoryMapper.selectById(article.getCategoryId());
        String categoryName = null;
        if (Objects.nonNull(category)) {
            categoryName = category.getCategoryName();
        }
        // 查询文章标签
        List<ArticleTag> articleTagIds = articleTagMapper.selectList(new LambdaQueryWrapper<ArticleTag>()
                .select(ArticleTag::getTagId)
                .eq(ArticleTag::getArticleId, articleId));
        List<String> tagNameList = articleTagIds.stream().map(v ->
                        tagMapper.selectOne(new LambdaQueryWrapper<Tag>().eq(Tag::getId, v.getTagId())).getTagName())
                .collect(Collectors.toList());
        ArticleVO articleVO = BeanCopyUtil.copyObject(article, ArticleVO.class);
        articleVO.setCategoryName(categoryName);
        articleVO.setTagNameList(tagNameList);
        return articleVO;
    }

}
