package com.star.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.RedisUtil;
import com.star.core.config.RabbitConfig;
import com.star.core.dto.*;
import com.star.core.entity.Article;
import com.star.core.entity.ArticleTag;
import com.star.core.entity.Category;
import com.star.core.entity.Tag;
import com.star.core.mapper.ArticleMapper;
import com.star.core.mapper.ArticleTagMapper;
import com.star.core.mapper.CategoryMapper;
import com.star.core.mapper.TagMapper;
import com.star.core.search.ArticleMqMessage;
import com.star.core.service.ArticleService;
import com.star.core.service.ArticleTagService;
import com.star.core.service.TagService;
import com.star.core.strategy.context.SearchStrategyContext;
import com.star.core.util.BeanCopyUtil;
import com.star.core.util.PageUtils;
import com.star.core.util.UserUtil;
import com.star.core.vo.ArticleVO;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.DeleteVO;
import com.star.core.vo.TopVO;
import org.apache.commons.collections.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.ARTICLE_SET;
import static com.star.common.constant.CommonConst.FALSE;
import static com.star.common.constant.RedisConst.*;
import static com.star.common.enums.ArticleStatusEnum.DRAFT;
import static com.star.common.enums.ArticleStatusEnum.PUBLIC;

/**
 * @Author: zzStar
 * @Date: 12-19-2020 13:37
 */
@Service
@SuppressWarnings("unchecked")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    private static final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private static final String LAST_SQL_FIVE = "limit 5";
    private static final String LAST_SQL_ONE = "limit 1";

    @Resource
    private SearchStrategyContext searchStrategyContext;
    @Resource
    private TagService tagService;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private AmqpTemplate amqpTemplate;
    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private CategoryMapper categoryMapper;
    @Resource
    private TagMapper tagMapper;
    @Resource
    private ArticleTagMapper articleTagMapper;
    @Resource
    private HttpSession session;
    @Resource
    private ArticleService articleService;
    @Resource
    private ArticleTagService articleTagService;

    @Override
    public PageData<ArchiveDTO> listArchives() {
        Page<Article> page = new Page<>(PageUtils.getLimitCurrent(), PageUtils.getSize());
        Page<Article> articlePage = articleMapper.selectPage(page, new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getCreateTime)
                .orderByDesc(Article::getCreateTime)
                .eq(Article::getStatus, PUBLIC.getStatus())
                .eq(Article::getIsDelete, FALSE));

        // 拷贝dto集合
        List<ArchiveDTO> archiveList = BeanCopyUtil.copyList(articlePage.getRecords(), ArchiveDTO.class);
        return new PageData<>(archiveList, articlePage.getTotal());
    }

    @Override
    public PageData<ArticleBackDTO> listArticleBack(ConditionVO condition) {
        // 查询文章总量
        Long count = articleMapper.countArticlesBack(condition);
        if (count == 0) {
            return new PageData<>();
        }
        // 查询后台文章
        List<ArticleBackDTO> articleBackList = articleMapper.listArticlesBack(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        // 查询文章点赞量和浏览量
        Map<String, Object> likeCountMap = redisUtil.hGetAll(ARTICLE_LIKE_COUNT);
        Map<Object, Double> viewsCountMap = redisUtil.zAllScore(ARTICLE_VIEWS_COUNT);
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

    @Override
    public List<ArticleHomeDTO> listArticles() {
        return articleMapper.listArticles(PageUtils.getLimitCurrent(), PageUtils.getSize());
    }

    @Override
    public ArticlePreviewListDTO listArticlesByCondition(ConditionVO condition) {
        // 搜索条件对应数据
        List<ArticlePreviewDTO> articlePreviewList = articleMapper.listArticlesByCondition(
                PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        // 搜索条件对应名(标签或分类名)
        String name;
        if (Objects.nonNull(condition.getCategoryId())) {
            name = categoryMapper.selectOne(new LambdaQueryWrapper<Category>()
                            .select(Category::getCategoryName)
                            .eq(Category::getId, condition.getCategoryId()))
                    .getCategoryName();
        } else {
            name = tagMapper.selectOne(new LambdaQueryWrapper<Tag>()
                            .select(Tag::getTagName)
                            .eq(Tag::getId, condition.getTagId()))
                    .getTagName();
        }
        return ArticlePreviewListDTO.builder()
                .articlePreviewList(articlePreviewList)
                .name(name).build();
    }

    @Override
    public ArticleDTO getArticleById(Integer articleId) {
        // 查询id对应的文章
        ArticleDTO article = articleMapper.getArticleById(articleId);
        if (Objects.isNull(article)) {
            throw new StarryException("文章不存在");
        }
        // 更新文章浏览量
        updateArticleViewsCount(articleId);

        // 查询推荐文章
        CompletableFuture<List<ArticleRecommendDTO>> recommendArticleList = CompletableFuture.supplyAsync(() ->
                        articleMapper.listArticleRecommends(articleId))
                .whenComplete((v, t) -> article.setArticleRecommendList(v));
        // 查询最新文章
        CompletableFuture<List<ArticleRecommendDTO>> newestArticleList = CompletableFuture.supplyAsync(() -> {
            List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                    .select(Article::getId, Article::getArticleTitle, Article::getArticleCover, Article::getCreateTime)
                    .eq(Article::getIsDelete, FALSE)
                    .eq(Article::getStatus, PUBLIC.getStatus())
                    .orderByDesc(Article::getCreateTime)
                    .last(LAST_SQL_FIVE));
            return BeanCopyUtil.copyList(articleList, ArticleRecommendDTO.class);
        }).whenComplete((v, t) -> article.setNewestArticleList(v));
        CompletableFuture.allOf(newestArticleList, recommendArticleList).join();

        // 查询上一篇下一篇文章
        Article lastArticle = getLastArticle(articleId);
        Article nextArticle = getNextArticle(articleId);
        article.setLastArticle(BeanCopyUtil.copyObject(lastArticle, ArticlePaginationDTO.class));
        article.setNextArticle(BeanCopyUtil.copyObject(nextArticle, ArticlePaginationDTO.class));

        // 封装点赞量和浏览量封装
        Double viewCount = redisUtil.zScore(ARTICLE_VIEWS_COUNT, articleId);
        if (Objects.nonNull(viewCount)) {
            article.setViewsCount(viewCount.intValue());
        }
        article.setLikeCount((Integer) redisUtil.hGet(ARTICLE_LIKE_COUNT, articleId.toString()));

        return article;
    }

    private Article getLastArticle(Integer articleId) {
        return articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, PUBLIC.getStatus())
                .lt(Article::getId, articleId)
                .orderByDesc(Article::getId)
                .last(LAST_SQL_ONE));
    }

    private Article getNextArticle(Integer articleId) {
        return articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getStatus, FALSE)
                .gt(Article::getId, articleId)
                .orderByAsc(Article::getId)
                .last(LAST_SQL_ONE));
    }

    /**
     * 更新文章浏览量
     *
     * @param articleId 文章id
     */
    @Async
    public void updateArticleViewsCount(Integer articleId) {
        // 判断是否第一次访问，增加浏览量
        Set<Integer> articleSet = (Set<Integer>) Optional.ofNullable(session.getAttribute(ARTICLE_SET))
                .orElse(new HashSet<>());
        if (!articleSet.contains(articleId)) {
            articleSet.add(articleId);
            session.setAttribute(ARTICLE_SET, articleSet);
            // +1
            redisUtil.zIncr(ARTICLE_VIEWS_COUNT, articleId, 1D);
        }
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void saveArticleLike(Integer articleId) {
        String likeKey = ARTICLE_USER_LIKE + UserUtil.getLoginUser().getUserInfoId();
        if (redisUtil.sIsMember(likeKey, articleId)) {
            // 点过赞则删除文章id
            redisUtil.sRemove(likeKey, articleId);
            redisUtil.hDecr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        } else {
            redisUtil.sAdd(likeKey, articleId);
            redisUtil.hIncr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        }
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void saveOrUpdateArticle(ArticleVO articleVO) {
        // 保存文章分类
        Category category = saveArticleCategory(articleVO);
        // 保存或修改文章
        Article article = BeanCopyUtil.copyObject(articleVO, Article.class);
        if (Objects.nonNull(category)) {
            article.setCategoryId(category.getId());
        }
        article.setUserId(UserUtil.getUserInfoId());
        articleService.saveOrUpdate(article);
        // 保存文章标签
        saveArticleTag(articleVO, article.getId());
        // es
        amqpTemplate.convertAndSend(RabbitConfig.ES_CHANGE,
                RabbitConfig.ES_BIND_KEY, new ArticleMqMessage(article.getId(), ArticleMqMessage.CREATE_OR_UPDATE));
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
        List<Tag> existTagList = tagService.list(new LambdaQueryWrapper<Tag>().in(Tag::getTagName, tagNameList));
        List<String> existTagNameList = existTagList.stream().map(Tag::getTagName).collect(Collectors.toList());
        List<Integer> existTagIdList = existTagList.stream().map(Tag::getId).collect(Collectors.toList());

        // 对比新增不存在的标签
        tagNameList.removeAll(existTagNameList);
        // 剩下的为新增的
        if (CollectionUtils.isNotEmpty(tagNameList)) {
            List<Tag> tagList = tagNameList.stream().map(v ->
                            Tag.builder().tagName(v).build())
                    .collect(Collectors.toList());
            tagService.saveBatch(tagList);
            List<Integer> tagIdList = tagList.stream()
                    .map(Tag::getId)
                    .collect(Collectors.toList());
            existTagIdList.addAll(tagIdList);
        }
        // 关联表维护
        List<ArticleTag> articleTagList = existTagIdList.stream().map(v ->
                        ArticleTag.builder().articleId(articleId).tagId(v).build())
                .collect(Collectors.toList());
        articleTagService.saveBatch(articleTagList);
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

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updateArticleTop(TopVO articleTopVO) {
        // 修改文章置顶状态
        Article article = Article.builder()
                .id(articleTopVO.getId())
                .isTop(articleTopVO.getIsTop()).build();
        articleMapper.updateById(article);
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updateArticleDelete(DeleteVO deleteVO) {
        // 修改文章逻辑删除状态
        List<Article> articleList = deleteVO.getIdList().stream().map(id ->
                Article.builder()
                        .id(id)
                        .isTop(FALSE)
                        .isDelete(deleteVO.getIsDelete()).build()).collect(Collectors.toList());
        articleService.updateBatchById(articleList);
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void deleteArticles(List<Integer> articleIdList) {
        // 删除文章标签关联
        articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                .in(ArticleTag::getArticleId, articleIdList));
        articleMapper.deleteBatchIds(articleIdList);

        // es
        amqpTemplate.convertAndSend(RabbitConfig.ES_CHANGE,
                RabbitConfig.ES_BIND_KEY, new ArticleMqMessage(articleIdList.get(0), ArticleMqMessage.REMOVE));
    }

    @Override
    public List<ArticleSearchDTO> listArticlesBySearch(ConditionVO condition) {
        return searchStrategyContext.executeSearchStrategy(condition.getKeywords());
    }

    @Override
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
