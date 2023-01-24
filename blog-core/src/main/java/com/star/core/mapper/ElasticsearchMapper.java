package com.star.core.mapper;

import com.star.core.dto.ArticleSearchDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

/**
 * @Description: es
 * @Author: zzStar
 * @Date: 12-21-2020 21:57
 */
@Component
public interface ElasticsearchMapper extends ElasticsearchRepository<ArticleSearchDTO, Integer> {
}
