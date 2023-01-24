package com.star.core.strategy.impl;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Highlight;
import co.elastic.clients.elasticsearch.core.search.HighlightField;
import com.star.core.dto.ArticleSearchDTO;
import com.star.core.strategy.SearchStrategy;
import jakarta.annotation.Resource;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.*;
import static com.star.common.enums.StatusEnum.PUBLIC;

/**
 * @Author: zzStar
 * @Date: 08-19-2021 23:12
 */
@Service("EsSearchStrategyImpl")
public class EsSearchStrategyImpl implements SearchStrategy {

    private static final Logger logger = LoggerFactory.getLogger(EsSearchStrategyImpl.class);
    public static final String ARTICLE_TITLE = "articleTitle";
    public static final String ARTICLE_CONTENT = "articleContent";
    public static final int CONTENT_SIZE = 200;
    public static final String IS_DELETE = "isDelete";
    public static final String STATUS = "status";

    @Resource
    private ElasticsearchClient elasticsearchClient;

    @Override
    public List<ArticleSearchDTO> searchArticle(String keywords) {
        List<ArticleSearchDTO> res = new ArrayList<>();
        try {
            if (StringUtils.isBlank(keywords)) {
                return res;
            }
            return searchArticle(buildQuery(keywords));
        } catch (IOException e) {
            logger.error("EsSearchStrategyImpl.searchArticle: ", e);
            return res;
        }
    }

    /**
     * 搜索文章
     *
     * @param keywords 关键字
     * @return /
     */
    private SearchResponse<ArticleSearchDTO> buildQuery(String keywords) throws IOException {
        // match 模糊匹配 进行分词,然后按分词匹配查找
        // term 精确查询 搜索前不会再对搜索词进行分词拆解
        co.elastic.clients.elasticsearch._types.query_dsl.Query titleQuery =
                MatchQuery.of(m -> m.field(ARTICLE_TITLE).query(keywords))._toQuery();
        co.elastic.clients.elasticsearch._types.query_dsl.Query contentQuery =
                MatchQuery.of(m -> m.field(ARTICLE_CONTENT).query(keywords))._toQuery();
        co.elastic.clients.elasticsearch._types.query_dsl.Query deleteQuery =
                TermQuery.of(t -> t.field(IS_DELETE).value(FALSE))._toQuery();
        co.elastic.clients.elasticsearch._types.query_dsl.Query statusQuery =
                TermQuery.of(t -> t.field(STATUS).value(PUBLIC.getStatus()))._toQuery();

        // 存放高亮的字段，默认与文档字段一致
        HighlightField hf = new HighlightField.Builder().build();
        Highlight titleAndContent = new Highlight.Builder().
                preTags(PRE_TAG).postTags(SPAN).
                fields(ARTICLE_TITLE, hf).
                fields(ARTICLE_CONTENT, hf)
                .fragmentSize(CONTENT_SIZE).build();

        return elasticsearchClient.search(s -> s.index("starry-blog").query(
                q -> q.bool(
                        b -> b.should(titleQuery, contentQuery)
                                .must(deleteQuery, statusQuery)
                )
        ).highlight(titleAndContent), ArticleSearchDTO.class);
    }

    /**
     * 搜索结果
     *
     * @param response /
     * @return 搜索结果
     */
    private List<ArticleSearchDTO> searchArticle(SearchResponse<ArticleSearchDTO> response) {
        try {
            return response.hits().hits().stream().map(hit -> {
                ArticleSearchDTO article = hit.source();
                // 获取文章标题高亮数据
                List<String> titleHighList = hit.highlight().get(ARTICLE_TITLE);
                if (CollectionUtils.isNotEmpty(titleHighList)) {
                    // 替换标题数据
                    article.setArticleTitle(titleHighList.get(0));
                }
                // 获取文章内容高亮数据
                List<String> contentHighList = hit.highlight().get(ARTICLE_CONTENT);
                if (CollectionUtils.isNotEmpty(contentHighList)) {
                    // 替换标题数据
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
