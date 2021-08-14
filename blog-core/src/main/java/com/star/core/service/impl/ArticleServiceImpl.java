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
import com.star.core.util.BeanCopyUtil;
import com.star.core.util.PageUtils;
import com.star.core.util.UserUtil;
import com.star.core.vo.ArticleTopVO;
import com.star.core.vo.ArticleVO;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.DeleteVO;
import org.apache.commons.collections.CollectionUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
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

/**
 * @Author: zzStar
 * @Date: 12-19-2020 13:37
 */
@Service
@SuppressWarnings("unchecked")
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

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
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Resource
    private HttpSession session;

    @Resource
    private ArticleService articleService;

    @Resource
    private ArticleTagService articleTagService;

    @Override
    public List<ArticleRecommendDTO> listNewestArticles() {
        // newest
        List<Article> articleList = articleMapper.selectList(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover, Article::getCreateTime)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getIsDraft, FALSE)
                .orderByDesc(Article::getId)
                .last("limit 5"));
        return BeanCopyUtil.copyList(articleList, ArticleRecommendDTO.class);
    }

    @Override
    public PageDTO<ArchiveDTO> listArchives(Long current) {
        Page<Article> page = new Page<>(current, 10);
        Page<Article> articlePage = articleMapper.selectPage(page, new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getCreateTime)
                .orderByDesc(Article::getCreateTime)
                .eq(Article::getIsDraft, FALSE)
                .eq(Article::getIsDelete, FALSE));

        // 拷贝dto集合
        List<ArchiveDTO> archiveDTOList = BeanCopyUtil.copyList(articlePage.getRecords(), ArchiveDTO.class);
        return new PageDTO<>(archiveDTOList, (int) articlePage.getTotal());
    }


    @Override
    public PageDTO<ArticleBackDTO> listArticleBack(ConditionVO condition) {
        // 转换页码
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        // 查询文章总量
        Integer count = articleMapper.countArticlesBack(condition);
        if (count == 0) {
            return new PageDTO<>();
        }
        // 查询后台文章
        List<ArticleBackDTO> articleBackDTOList = articleMapper.listArticlesBack(condition);
        // 查询文章点赞量和浏览量
        Map<String, Integer> likeCountMap = redisUtil.hGetAll(ARTICLE_LIKE_COUNT);
        Map<String, Integer> viewsCountMap = redisUtil.hGetAll(ARTICLE_VIEWS_COUNT);
        // 封装点赞量和浏览量
        articleBackDTOList.forEach(articleBackDTO -> {
            articleBackDTO.setViewsCount(Objects.requireNonNull(viewsCountMap).get(articleBackDTO.getId().toString()));
            articleBackDTO.setLikeCount(Objects.requireNonNull(likeCountMap).get(articleBackDTO.getId().toString()));
        });
        return new PageDTO<>(articleBackDTOList, count);
    }

    @Override
    public List<ArticleHomeDTO> listArticles() {
        return articleMapper.listArticles(PageUtils.getLimitCurrent(), PageUtils.getSize());
    }

    @Override
    public ArticlePreviewListDTO listArticlesByCondition(ConditionVO condition) {
        // 转换页码
        condition.setCurrent((condition.getCurrent() - 1) * 9);
        // 搜索条件对应数据
        List<ArticlePreviewDTO> articlePreviewDTOList = articleMapper.listArticlesByCondition(condition);
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
                .articlePreviewDTOList(articlePreviewDTOList)
                .name(name).build();
    }

    @Override
    public ArticleDTO getArticleById(Integer articleId) {
        // 查询推荐文章
        CompletableFuture<List<ArticleRecommendDTO>> recommendArticleList = CompletableFuture.supplyAsync(() ->
                articleMapper.listArticleRecommends(articleId));
        // 更新文章浏览量
        updateArticleViewsCount(articleId);
        // 查询id对应的文章
        ArticleDTO article = articleMapper.getArticleById(articleId);

        // 查询上一篇下一篇文章
        Article lastArticle = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getIsDraft, FALSE)
                .lt(Article::getId, articleId)
                .orderByDesc(Article::getId)
                .last("limit 1"));

        Article nextArticle = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleCover)
                .eq(Article::getIsDelete, FALSE)
                .eq(Article::getIsDraft, FALSE)
                .gt(Article::getId, articleId)
                .orderByAsc(Article::getId)
                .last("limit 1"));

        article.setLastArticle(BeanCopyUtil.copyObject(lastArticle, ArticlePaginationDTO.class));
        article.setNextArticle(BeanCopyUtil.copyObject(nextArticle, ArticlePaginationDTO.class));

        // 查询相关推荐文章
        article.setArticleRecommendList(articleMapper.listArticleRecommends(articleId));

        // 封装点赞量和浏览量封装
        article.setViewsCount((Integer) redisUtil.hGet(ARTICLE_VIEWS_COUNT, articleId.toString()));
        article.setLikeCount((Integer) redisUtil.hGet(ARTICLE_LIKE_COUNT, articleId.toString()));

        // 封装相关推荐文章
        try {
            article.setArticleRecommendList(recommendArticleList.get());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return article;
    }

    /**
     * 更新文章浏览量
     *
     * @param articleId 文章id
     */
    @Async
    public void updateArticleViewsCount(Integer articleId) {
        // 判断是否第一次访问，增加浏览量
        Set<Integer> articleSet = (Set<Integer>) session.getAttribute("articleSet");
        if (Objects.isNull(articleSet)) {
            articleSet = new HashSet<>();
        }
        if (!articleSet.contains(articleId)) {
            articleSet.add(articleId);
            session.setAttribute(ARTICLE_SET, articleSet);
            // +1
            redisUtil.hIncr(ARTICLE_VIEWS_COUNT, articleId.toString(), 1L);
        }
    }

    @Override
    public ArticleOptionDTO listArticleOptionDTO() {
        // 查询文章对应的分类
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>()
                .select(Category::getId, Category::getCategoryName));
        List<CategoryBackDTO> categoryBackDTOList = BeanCopyUtil.copyList(categoryList, CategoryBackDTO.class);
        // 查询文章对应的标签
        List<Tag> tagList = tagMapper.selectList(new LambdaQueryWrapper<Tag>()
                .select(Tag::getId, Tag::getTagName));
        List<TagDTO> tagDTOList = BeanCopyUtil.copyList(tagList, TagDTO.class);
        return ArticleOptionDTO.builder()
                .categoryDTOList(categoryBackDTOList)
                .tagDTOList(tagDTOList).build();
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void saveArticleLike(Integer articleId) {
        String likeKey = ARTICLE_USER_LIKE + UserUtil.getLoginUser().getUserInfoId();
        if (redisUtil.sIsMember(likeKey, articleId)) {
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
        // save Or update
        Article article = Article.builder()
                .id(articleVO.getId())
                .userId(UserUtil.getLoginUser().getUserInfoId())
                .categoryId(articleVO.getCategoryId())
                .articleCover(articleVO.getArticleCover())
                .articleTitle(articleVO.getArticleTitle())
                .articleContent(articleVO.getArticleContent())
                .createTime(Objects.isNull(articleVO.getId()) ? new Date() : null)
                .updateTime(Objects.nonNull(articleVO.getId()) ? new Date() : null)
                .isTop(articleVO.getIsTop())
                .isDraft(articleVO.getIsDraft()).build();
        articleService.saveOrUpdate(article);

        //  编辑文章则删除文章对应的所有标签
        if (Objects.nonNull(articleVO.getId()) && articleVO.getIsDraft().equals(FALSE)) {
            articleTagMapper.delete(new LambdaQueryWrapper<ArticleTag>()
                    .eq(ArticleTag::getArticleId, articleVO.getId()));
        }

        // 添加文章标签
        if (!articleVO.getTagIdList().isEmpty()) {
            List<ArticleTag> articleTagList = articleVO.getTagIdList().stream().map(tagId -> ArticleTag.builder()
                            .articleId(article.getId())
                            .tagId(tagId).build())
                    .collect(Collectors.toList());
            articleTagService.saveBatch(articleTagList);
        }

        // es
        amqpTemplate.convertAndSend(RabbitConfig.ES_CHANGE,
                RabbitConfig.ES_BIND_KEY, new ArticleMqMessage(article.getId(), ArticleMqMessage.CREATE_OR_UPDATE));
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updateArticleTop(ArticleTopVO articleTopVO) {
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
        List<Article> articleList = deleteVO.getIdList().stream().map(id -> Article.builder()
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
        return searchArticle(buildQuery(condition));
    }

    @Override
    public ArticleVO getArticleBackById(Integer articleId) {
        // 查询文章信息
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getArticleContent
                        , Article::getArticleCover, Article::getCategoryId, Article::getIsTop, Article::getIsDraft)
                .eq(Article::getId, articleId));

        // 查询文章标签
        List<Integer> articleTagList = articleTagMapper.selectList(new LambdaQueryWrapper<ArticleTag>()
                        .select(ArticleTag::getTagId)
                        .eq(ArticleTag::getArticleId, article.getId()))
                .stream()
                .map(ArticleTag::getTagId).collect(Collectors.toList());
        return ArticleVO.builder()
                .id(article.getId())
                .articleTitle(article.getArticleTitle())
                .articleContent(article.getArticleContent())
                .articleCover(article.getArticleCover())
                .categoryId(article.getCategoryId())
                .isTop(article.getIsTop())
                .isDraft(article.getIsDraft())
                .tagIdList(articleTagList).build();
    }

    /**
     * 搜索文章构造
     *
     * @param condition 条件
     * @return es条件构造器
     */
    private NativeSearchQueryBuilder buildQuery(ConditionVO condition) {
        // 条件构造器
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 根据关键词搜索文章标题或内容
        if (Objects.nonNull(condition.getKeywords())) {
            boolQueryBuilder.must(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("articleTitle", condition.getKeywords()))
                            .should(QueryBuilders.matchQuery("articleContent", condition.getKeywords())))
                    .must(QueryBuilders.termQuery("isDelete", FALSE));
        }
        // 查询
        nativeSearchQueryBuilder.withQuery(boolQueryBuilder);
        return nativeSearchQueryBuilder;
    }

    /**
     * 文章搜索结果高亮
     *
     * @param nativeSearchQueryBuilder es条件构造器
     * @return 搜索结果
     */
    private List<ArticleSearchDTO> searchArticle(NativeSearchQueryBuilder nativeSearchQueryBuilder) {
        // 添加文章标题高亮
        HighlightBuilder.Field titleField = new HighlightBuilder.Field("articleTitle");
        titleField.preTags("<span style='color:#f47466'>");
        titleField.postTags("</span>");
        // 添加文章内容高亮
        HighlightBuilder.Field contentField = new HighlightBuilder.Field("articleContent");
        contentField.preTags("<span style='color:#f47466'>");
        contentField.postTags("</span>");
        contentField.fragmentSize(200);
        nativeSearchQueryBuilder.withHighlightFields(titleField, contentField);
        // 搜索
        SearchHits<ArticleSearchDTO> search = elasticsearchRestTemplate.search(nativeSearchQueryBuilder.build(), ArticleSearchDTO.class);

        return search.getSearchHits().stream().map(hit -> {
            ArticleSearchDTO article = hit.getContent();
            // 获取文章标题高亮数据
            List<String> titleHighList = hit.getHighlightFields().get("articleTitle");
            if (CollectionUtils.isNotEmpty(titleHighList)) {
                // 替换标题数据
                article.setArticleTitle(titleHighList.get(0));
            }
            // 获取文章内容高亮数据
            List<String> contentHighList = hit.getHighlightFields().get("articleContent");
            if (CollectionUtils.isNotEmpty(contentHighList)) {
                // 替换内容数据
                article.setArticleContent(contentHighList.get(0));
            }
            return article;
        }).collect(Collectors.toList());
    }

}
