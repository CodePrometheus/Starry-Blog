package com.star.inf.search;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.common.tool.BeanCopyUtil;
import com.star.inf.dto.ArticleSearchDTO;
import com.star.inf.entity.Article;
import com.star.inf.mapper.ArticleMapper;
import com.star.inf.mapper.ElasticsearchMapper;
import jakarta.annotation.Resource;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-02-2021 23:30
 */
@Component
public class ElasticSearchUtil {

    private static final Logger log = LoggerFactory.getLogger(ElasticSearchUtil.class);

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ElasticsearchMapper elasticsearchMapper;

    @Async
    public void async() {
        elasticsearchMapper.deleteAll();
        List<Article> articles = articleMapper.selectList(null);
        List<ArticleSearchDTO> articleSearch = new ArrayList<>();
        articles.forEach(article -> {
            ArticleSearchDTO search = ArticleSearchDTO.builder().id(String.valueOf(article.getId()))
                    .articleContent(article.getArticleContent())
                    .articleTitle(article.getArticleTitle())
                    .isDelete(article.getIsDelete())
                    .status(article.getStatus()).build();
            articleSearch.add(search);
        });
        elasticsearchMapper.saveAll(articleSearch);
    }

    @Async
    public void save(Article article) {
        ArticleSearchDTO articleSearch = new ArticleSearchDTO();
        BeanCopyUtil.copyObject(article, ArticleSearchDTO.class);
        elasticsearchMapper.save(articleSearch);
    }

    @Async
    public void createOrUpdateIndex(ArticleMqMessage mqMessage) {
        Integer articleId = mqMessage.getArticleId();
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>().eq(Article::getId, articleId));
        ModelMapper modelMapper = new ModelMapper();
        ArticleSearchDTO map = modelMapper.map(article, ArticleSearchDTO.class);
        elasticsearchMapper.save(map);
        log.info("index updated successfully --> {}", map.getArticleTitle());
    }

    @Async
    public void removeIndex(ArticleMqMessage mqMessage) {
        int articleId = mqMessage.getArticleId();
        elasticsearchMapper.deleteById(articleId);
        log.info("index remove successfully --> {}", mqMessage.getArticleId());
    }

}
