package com.star.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.constant.ArticleConst;
import com.star.constant.DeleteConst;
import com.star.domain.entity.Article;
import com.star.domain.entity.ArticleTag;
import com.star.domain.entity.Category;
import com.star.domain.entity.Tag;
import com.star.domain.mapper.ArticleMapper;
import com.star.domain.mapper.ArticleTagMapper;
import com.star.domain.mapper.CategoryMapper;
import com.star.domain.mapper.TagMapper;
import com.star.domain.vo.ArticleVO;
import com.star.domain.vo.ConditionVO;
import com.star.domain.vo.DeleteVO;
import com.star.exception.StarryException;
import com.star.service.ArticleService;
import com.star.service.ArticleTagService;
import com.star.service.dto.*;
import com.star.util.BeanCopyUtil;
import com.star.util.HTMLUtil;
import com.star.util.UserUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.beanutils.PropertyUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.common.text.Text;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpSession;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * @Author: zzStar
 * @Date: 12-19-2020 13:37
 */
@Service
@Slf4j
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private TagMapper tagMapper;

    @Autowired
    private ArticleTagMapper articleTagMapper;

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private HttpSession session;

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public PageDTO<ArchiveDTO> listArchives(Long current) {
        Page<Article> page = new Page<>(current, 10);
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "article_title", "create_time").orderByDesc("create_time").eq("is_delete", DeleteConst.NORMAL).eq("is_draft", ArticleConst.PUBLISH);
        //获取分页数据
        Page<Article> articlePage = articleMapper.selectPage(page, queryWrapper);
        //拷贝dto集合
        List<ArchiveDTO> archiveDTOList = BeanCopyUtil.copyList(articlePage.getRecords(), ArchiveDTO.class);
        return new PageDTO<ArchiveDTO>(archiveDTOList, (int) articlePage.getTotal());
    }

    @Override
    public PageDTO<ArticleBackDTO> listArticleBackDTO(ConditionVO condition) {
        //转换页码
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        //查询文章总量
        Integer count = articleMapper.countArticlesBack(condition);
        if (count == 0) {
            return new PageDTO<ArticleBackDTO>();
        }
        //查询后台文章
        List<ArticleBackDTO> articleBackDTOList = articleMapper.listArticlesBack(condition);
        //查询文章点赞量和浏览量
        Map<String, Integer> viewsCountMap = redisTemplate.boundHashOps("article_views_count").entries();
        Map<String, Integer> likeCountMap = redisTemplate.boundHashOps("article_like_count").entries();
        //封装点赞量和浏览量
        for (ArticleBackDTO articleBackDTO : articleBackDTOList) {
            articleBackDTO.setViewsCount(viewsCountMap.get(articleBackDTO.getId().toString()));
            articleBackDTO.setLikeCount(likeCountMap.get(articleBackDTO.getId().toString()));
        }
        return new PageDTO<ArticleBackDTO>(articleBackDTOList, count);
    }

    @Override
    public List<ArticleHomeDTO> listArticles(Long current) {
        //转换页码查询
        List<ArticleHomeDTO> articleDTOList = articleMapper.listArticles((current - 1) * 10);
        //文章内容过滤markdown标签展示
        for (ArticleHomeDTO articleDTO : articleDTOList) {
            articleDTO.setArticleContent(HTMLUtil.deleteArticleTag(articleDTO.getArticleContent()));
        }
        return articleDTOList;
    }

    @Override
    public ArticlePreviewListDTO listArticlesByCondition(ConditionVO condition) {
        //转换页码
        condition.setCurrent((condition.getCurrent() - 1) * 9);
        //搜索条件对应数据
        List<ArticlePreviewDTO> articlePreviewDTOList = articleMapper.listArticlesByCondition(condition);
        //搜索条件对应名(标签或分类名)
        String name = null;
        if (condition.getCategoryId() != null) {
            QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("category_name").eq("id", condition.getCategoryId());
            name = categoryMapper.selectOne(queryWrapper).getCategoryName();
        } else {
            QueryWrapper<Tag> queryWrapper = new QueryWrapper<>();
            queryWrapper.select("tag_name").eq("id", condition.getTagId());
            name = tagMapper.selectOne(queryWrapper).getTagName();
        }
        return new ArticlePreviewListDTO(articlePreviewDTOList, name);
    }

    @Override
    public ArticleDTO getArticleById(Integer articleId) {
        //判断是否第一次访问，增加浏览量
        Set<Integer> set = (Set<Integer>) session.getAttribute("articleSet");
        if (set == null) {
            set = new HashSet<>();
        }
        if (!set.contains(articleId)) {
            set.add(articleId);
            session.setAttribute("articleSet", set);
            //浏览量+1
            redisTemplate.boundHashOps("article_views_count").increment(articleId.toString(), 1);
        }
        //查询id对应的文章
        ArticleDTO article = articleMapper.getArticleById(articleId);
        //封装点赞量和浏览量封装
        article.setViewsCount((Integer) redisTemplate.boundHashOps("article_views_count").get(articleId.toString()));
        article.setLikeCount((Integer) redisTemplate.boundHashOps("article_like_count").get(articleId.toString()));
        return article;
    }

    @Override
    public ArticleOptionDTO listArticleOptionDTO() {
        //查询文章分类选项
        QueryWrapper<Category> categoryQueryWrapper = new QueryWrapper<>();
        categoryQueryWrapper.select("id", "category_name");
        List<CategoryBackDTO> categoryDTOList = BeanCopyUtil.copyList(categoryMapper.selectList(categoryQueryWrapper), CategoryBackDTO.class);
        //查询文章标签选项
        QueryWrapper<Tag> tagQueryWrapper = new QueryWrapper<>();
        tagQueryWrapper.select("id", "tag_name");
        List<TagDTO> tagDTOList = BeanCopyUtil.copyList(tagMapper.selectList(tagQueryWrapper), TagDTO.class);
        return new ArticleOptionDTO(categoryDTOList, tagDTOList);
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void saveArticleLike(Integer articleId) {
        //查询当前用户点赞过的文章id集合
        Set<Integer> articleLikeSet = (Set<Integer>) redisTemplate.boundHashOps("article_user_like").get(UserUtil.getLoginUser().getUserInfoId().toString());
        //第一次点赞则创建
        if (articleLikeSet == null) {
            articleLikeSet = new HashSet<Integer>();
        }
        //判断是否点赞
        if (articleLikeSet.contains(articleId)) {
            //点过赞则删除文章id
            articleLikeSet.remove(articleId);
            //文章点赞量-1
            redisTemplate.boundHashOps("article_like_count").increment(articleId.toString(), -1);
        } else {
            //未点赞则增加文章id
            articleLikeSet.add(articleId);
            //文章点赞量+1
            redisTemplate.boundHashOps("article_like_count").increment(articleId.toString(), 1);
        }
        //保存点赞记录
        redisTemplate.boundHashOps("article_user_like").put(UserUtil.getLoginUser().getUserInfoId().toString(), articleLikeSet);
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void saveOrUpdateArticle(ArticleVO articleVO) {
        Article article = new Article(articleVO);
        //编辑文章则删除文章所有标签
        if (articleVO.getId() != null && articleVO.getIsDraft() == ArticleConst.PUBLISH) {
            QueryWrapper<ArticleTag> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("article_id", articleVO.getId());
            articleTagMapper.delete(queryWrapper);
        }
        articleService.saveOrUpdate(article);
        //添加文章标签
        if (!articleVO.getTagIdList().isEmpty()) {
            List<ArticleTag> articleTagList = new ArrayList<>();
            for (Integer tagId : articleVO.getTagIdList()) {
                articleTagList.add(new ArticleTag(article.getId(), tagId));
            }
            articleTagService.saveBatch(articleTagList);
        }
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void updateArticleTop(Integer articleId, Integer isTop) {
        //修改文章置顶状态
        articleMapper.updateById(new Article(articleId, isTop));
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void updateArticleDelete(DeleteVO deleteVO) {
        //修改文章逻辑删除状态
        List<Article> articleList = new ArrayList<>();
        for (Integer articleId : deleteVO.getIdList()) {
            Article article = new Article(articleId);
            article.setIsDelete(deleteVO.getIsDelete());
            articleList.add(article);
        }
        articleService.updateBatchById(articleList);
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void deleteArticles(List<Integer> articleIdList) {
        //删除文章标签关联
        QueryWrapper<ArticleTag> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("article_id", articleIdList);
        articleTagMapper.delete(queryWrapper);
        //删除文章
        articleMapper.deleteBatchIds(articleIdList);
    }


    @Override
    public List<ArticleSearchDTO> listArticlesBySearch(ConditionVO condition) {
        return searchArticle(buildQuery(condition));
    }

    @Override
    public ArticleVO getArticleBackById(Integer articleId) {
        //查询文章信息
        QueryWrapper<Article> articleQueryWrapper = new QueryWrapper<>();
        articleQueryWrapper.select("id", "article_title", "article_content", "article_cover", "category_id", "is_top", "is_draft").eq("id", articleId);
        Article article = articleMapper.selectOne(articleQueryWrapper);
        //查询文章标签
        QueryWrapper<ArticleTag> articleTagQueryWrapper = new QueryWrapper<>();
        articleTagQueryWrapper.select("tag_id").eq("article_id", article.getId());
        List<ArticleTag> articleTagList = articleTagMapper.selectList(articleTagQueryWrapper);
        //提取标签id集合
        List<Integer> tagIdList = new ArrayList<>();
        for (ArticleTag articleTag : articleTagList) {
            tagIdList.add(articleTag.getTagId());
        }
        return new ArticleVO(article, tagIdList);
    }

    /**
     * 搜索文章构造
     *
     * @param condition 条件
     * @return es条件构造器
     */
    private NativeSearchQueryBuilder buildQuery(ConditionVO condition) {
        //条件构造器
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        //根据关键词搜索文章标题或内容
        if (condition.getKeywords() != null) {
            boolQueryBuilder.must(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("articleTitle", condition.getKeywords()))
                    .should(QueryBuilders.matchQuery("articleContent", condition.getKeywords())))
                    .must(QueryBuilders.termQuery("isDelete", DeleteConst.NORMAL));
        }
        //查询
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
        //添加文章标题高亮
        HighlightBuilder.Field titleField = new HighlightBuilder.Field("articleTitle");
        titleField.preTags("<span style='color:#f47466'>");
        titleField.postTags("</span>");
        //添加文章内容高亮
        HighlightBuilder.Field contentField = new HighlightBuilder.Field("articleContent");
        contentField.preTags("<span style='color:#f47466'>");
        contentField.postTags("</span>");
        contentField.fragmentSize(200);
        nativeSearchQueryBuilder.withHighlightFields(titleField, contentField);
        //搜索
        AggregatedPage<ArticleSearchDTO> page = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), ArticleSearchDTO.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> aClass, Pageable pageable) {
                List list = new ArrayList();
                for (SearchHit hit : response.getHits()) {
                    //获取所有数据
                    ArticleSearchDTO article = JSON.parseObject(hit.getSourceAsString(), ArticleSearchDTO.class);
                    //获取文章标题高亮数据
                    HighlightField titleField = hit.getHighlightFields().get("articleTitle");
                    if (titleField != null && titleField.getFragments() != null) {
                        //替换标题数据
                        article.setArticleTitle(titleField.getFragments()[0].toString());
                    }
                    //获取文章内容高亮数据
                    HighlightField contentField = hit.getHighlightFields().get("articleContent");
                    if (contentField != null && contentField.getFragments() != null) {
                        //替换内容数据
                        article.setArticleContent(contentField.getFragments()[0].toString());
                    }
                    list.add(article);
                }
                return new AggregatedPageImpl<T>(list, pageable, response.getHits().getTotalHits());
            }

            @Override
            public <T> T mapSearchHit(SearchHit searchHit, Class<T> clazz) {
                List<T> results = new ArrayList<>();
                for (HighlightField field : searchHit.getHighlightFields().values()) {
                    T result = null;
                    if (StringUtils.hasText(searchHit.getSourceAsString())) {
                        result = JSONObject.parseObject(searchHit.getSourceAsString(), clazz);
                    }
                    try {
                        PropertyUtils.setProperty(result, field.getName(), concat(field.fragments()));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        log.error("设置高亮字段异常：{}", e.getMessage(), e);
                    }
                    results.add(result);
                }
                return null;
            }
        });
        return page.getContent();
    }

    private String concat(Text[] texts) {
        StringBuffer sb = new StringBuffer();
        for (Text text : texts) {
            sb.append(text.toString());
        }
        return sb.toString();
    }

}
