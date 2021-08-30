package com.star.core.strategy.impl;

import com.star.core.dto.ArticleSearchDTO;
import com.star.core.strategy.SearchStrategy;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.*;
import static com.star.common.enums.ArticleStatusEnum.PUBLIC;

/**
 * @Author: zzStar
 * @Date: 08-19-2021 23:12
 */
@Service("EsSearchStrategyImpl")
public class EsSearchStrategyImpl implements SearchStrategy {

    private static final Logger logger = LoggerFactory.getLogger(EsSearchStrategyImpl.class);

    @Resource
    private ElasticsearchRestTemplate elasticsearchRestTemplate;

    @Override
    public List<ArticleSearchDTO> searchArticle(String keywords) {
        System.out.println("keywords = " + keywords);
        if (StringUtils.isBlank(keywords)) {
            return new ArrayList<>();
        }
        return searchArticle(buildQuery(keywords));
    }

    /**
     * 搜索文章构造
     *
     * @param keywords 关键字
     * @return es条件构造器
     */
    private NativeSearchQueryBuilder buildQuery(String keywords) {
        // 条件构造器
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        // 根据关键词搜索文章标题或内容
        boolQueryBuilder.must(QueryBuilders.boolQuery().should(QueryBuilders.matchQuery("articleTitle", keywords))
                        .should(QueryBuilders.matchQuery("articleContent", keywords)))
                .must(QueryBuilders.termQuery("isDelete", FALSE))
                .must(QueryBuilders.termQuery("status", PUBLIC.getStatus()));

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
        titleField.preTags(PRE_TAG);
        titleField.postTags(SPAN);
        // 添加文章内容高亮
        HighlightBuilder.Field contentField = new HighlightBuilder.Field("articleContent");
        contentField.preTags(PRE_TAG);
        contentField.postTags(SPAN);
        contentField.fragmentSize(200);
        nativeSearchQueryBuilder.withHighlightFields(titleField, contentField);

        try {
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
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return new ArrayList<>();
    }

}
