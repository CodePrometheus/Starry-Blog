package com.star.core.search;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.star.core.domain.entity.Article;
import com.star.core.domain.mapper.ArticleMapper;
import com.star.core.domain.mapper.ElasticsearchMapper;
import com.star.core.search.ArticleMqMessage;
import com.star.core.service.dto.ArticleSearchDTO;
import com.star.core.util.BeanCopyUtil;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-02-2021 23:30
 */
@Slf4j
@Component
public class ElasticSearchUtil {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private ElasticsearchMapper elasticsearchMapper;

    @Async
    public void async() {
        elasticsearchMapper.deleteAll();
        List<Article> articles = articleMapper.selectList(null);
        List<ArticleSearchDTO> articleSearch = new ArrayList<>();
        for (Article article : articles) {
            ArticleSearchDTO searchDTO = new ArticleSearchDTO();
            searchDTO.setId(article.getId());
            searchDTO.setArticleContent(article.getArticleContent());
            searchDTO.setArticleTitle(article.getArticleTitle());
            searchDTO.setIsDelete(article.getIsDelete());
            articleSearch.add(searchDTO);
        }
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


    public void createOrUpdateIndex(ArticleMqMessage mqMessage) {
        int articleId = mqMessage.getArticleId();
        Article article = articleMapper.selectOne(new QueryWrapper<Article>().eq("id", articleId));
        ModelMapper modelMapper = new ModelMapper();
        ArticleSearchDTO map = modelMapper.map(article, ArticleSearchDTO.class);
        elasticsearchMapper.save(map);
        log.info("index updated successfully --> {}", map.toString());
    }

    public void removeIndex(ArticleMqMessage mqMessage) {
        int articleId = mqMessage.getArticleId();
        elasticsearchMapper.deleteById(articleId);
        log.info("index remove successfully --> {}", mqMessage.toString());
    }
}
