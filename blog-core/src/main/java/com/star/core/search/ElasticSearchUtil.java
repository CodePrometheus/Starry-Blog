package com.star.core.search;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.core.dto.ArticleSearchDTO;
import com.star.core.entity.Article;
import com.star.core.mapper.ArticleMapper;
import com.star.core.mapper.ElasticsearchMapper;
import com.star.core.util.BeanCopyUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
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
            ArticleSearchDTO search = ArticleSearchDTO.builder().id(article.getId())
                    .articleContent(article.getArticleContent())
                    .articleTitle(article.getArticleTitle())
                    .isDelete(article.getIsDelete()).build();
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
    public void deleteById(int id) {
        elasticsearchMapper.deleteById(id);
    }

    @Async
    public void deleteAll(List<ArticleSearchDTO> articleSearch) {
        elasticsearchMapper.deleteAll(articleSearch);
    }


    @Async
    public void createOrUpdateIndex(ArticleMqMessage mqMessage) {
        Integer articleId = mqMessage.getArticleId();
        Article article = articleMapper.selectOne(new LambdaQueryWrapper<Article>().eq(Article::getId, articleId));
        ModelMapper modelMapper = new ModelMapper();
        ArticleSearchDTO map = modelMapper.map(article, ArticleSearchDTO.class);
        elasticsearchMapper.save(map);
        log.info("index updated successfully --> {}", map);
    }

    @Async
    public void removeIndex(ArticleMqMessage mqMessage) {
        int articleId = mqMessage.getArticleId();
        elasticsearchMapper.deleteById(articleId);
        log.info("index remove successfully --> {}", mqMessage.toString());
    }

}
