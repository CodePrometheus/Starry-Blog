package com.star.inf.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.BeanCopyUtils;
import com.star.common.tool.PageUtils;
import com.star.common.tool.RedisUtils;
import com.star.inf.dto.*;
import com.star.inf.entity.Article;
import com.star.inf.entity.Category;
import com.star.inf.entity.Tag;
import com.star.inf.mapper.ArticleMapper;
import com.star.inf.mapper.CategoryMapper;
import com.star.inf.mapper.TagMapper;
import com.star.inf.strategy.context.SearchStrategyContext;
import com.star.inf.utils.UserUtils;
import com.star.inf.vo.ConditionVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.CompletableFuture;

import static com.star.common.constant.CommonConst.ARTICLE_SET;
import static com.star.common.constant.CommonConst.FALSE;
import static com.star.common.constant.RedisConst.*;
import static com.star.common.enums.StatusEnum.PUBLIC;

/**
 * @Author: zzStar
 * @Date: 12-19-2020 13:37
 */
@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IService<Article> {

    private static final Logger log = LoggerFactory.getLogger(ArticleServiceImpl.class);

    private static final String LAST_SQL_FIVE = "limit 5";
    private static final String LAST_SQL_ONE = "limit 1";

    @Resource
    private SearchStrategyContext searchStrategyContext;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private TagMapper tagMapper;

    @Resource
    private HttpSession session;

    /**
     * 查询文章归档
     *
     * @return 文章
     */
    public PageData<ArchiveDTO> listArchives() {
        Page<Article> page = new Page<>(PageUtils.getLimitCurrent(), PageUtils.getSize());
        Page<Article> articlePage = articleMapper.selectPage(page, new LambdaQueryWrapper<Article>()
                .select(Article::getId, Article::getArticleTitle, Article::getCreateTime)
                .orderByDesc(Article::getCreateTime)
                .eq(Article::getStatus, PUBLIC.getStatus())
                .eq(Article::getIsDelete, FALSE));

        // 拷贝dto集合
        List<ArchiveDTO> archiveList = BeanCopyUtils.copyList(articlePage.getRecords(), ArchiveDTO.class);
        return new PageData<>(archiveList, articlePage.getTotal());
    }

    /**
     * 查看首页文章
     *
     * @return
     */
    public List<ArticleHomeDTO> listArticles() {
        return articleMapper.listArticles(PageUtils.getLimitCurrent(), PageUtils.getSize());
    }

    /**
     * 根据条件查询文章列表
     *
     * @param condition 条件
     * @return 文章
     */
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

    /**
     * 根据id查看文章
     *
     * @param articleId 文章id
     * @return 文章
     */
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
            return BeanCopyUtils.copyList(articleList, ArticleRecommendDTO.class);
        }).whenComplete((v, t) -> article.setNewestArticleList(v));
        CompletableFuture.allOf(newestArticleList, recommendArticleList).join();

        // 查询上一篇下一篇文章
        Article lastArticle = getLastArticle(articleId);
        Article nextArticle = getNextArticle(articleId);
        article.setLastArticle(BeanCopyUtils.copyObject(lastArticle, ArticlePaginationDTO.class));
        article.setNextArticle(BeanCopyUtils.copyObject(nextArticle, ArticlePaginationDTO.class));

        // 封装点赞量和浏览量封装
        Double viewCount = redisUtils.zScore(ARTICLE_VIEWS_COUNT, articleId);
        if (Objects.nonNull(viewCount)) {
            article.setViewsCount(viewCount.intValue());
        }
        article.setLikeCount((Integer) redisUtils.hGet(ARTICLE_LIKE_COUNT, articleId.toString()));

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
            redisUtils.zIncr(ARTICLE_VIEWS_COUNT, articleId, 1D);
        }
    }

    @Transactional(rollbackFor = StarryException.class)
    public void saveArticleLike(Integer articleId) {
        String likeKey = ARTICLE_USER_LIKE + UserUtils.getLoginUser().getUserInfoId();
        if (redisUtils.sIsMember(likeKey, articleId)) {
            // 点过赞则删除文章id
            redisUtils.sRemove(likeKey, articleId);
            redisUtils.hDecr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        } else {
            redisUtils.sAdd(likeKey, articleId);
            redisUtils.hIncr(ARTICLE_LIKE_COUNT, articleId.toString(), 1L);
        }
    }

    /**
     * Search Article
     *
     * @param condition
     * @return
     */
    public List<ArticleSearchDTO> listArticlesBySearch(ConditionVO condition) {
        return searchStrategyContext.executeSearchStrategy(condition.getKeywords());
    }

}
