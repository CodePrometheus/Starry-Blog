package com.star.core.search;

import com.star.core.dto.ArticleSearchDTO;
import com.star.core.entity.Article;
import com.star.core.mapper.ArticleMapper;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 2021/9/24
 * @Description:
 */
@Component
public class SolrSearchUtil {

    private static final Logger log = LoggerFactory.getLogger(ElasticSearchUtil.class);

    @Value("${spring.solr.core}")
    private String core;

    @Resource
    private ArticleMapper articleMapper;
    @Resource
    private SolrClient solrClient;

    @Async
    public void async() {
        List<Article> articles = articleMapper.selectList(null);
        articles.forEach(article -> {
            ArticleSearchDTO search = ArticleSearchDTO.builder()
                    .id(String.valueOf(article.getId()))
                    .articleContent(article.getArticleContent())
                    .articleTitle(article.getArticleTitle())
                    .isDelete(article.getIsDelete())
                    .status(article.getStatus()).build();
            try {
                solrClient.addBean(search);
                solrClient.commit();
            } catch (IOException | SolrServerException e) {
                log.error(e.getMessage());
            }
        });
    }


}
