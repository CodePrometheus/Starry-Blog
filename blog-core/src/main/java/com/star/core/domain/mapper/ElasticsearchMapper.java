package com.star.core.domain.mapper;

import com.star.core.service.dto.ArticleSearchDTO;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Description: es
 * @Author: zzStar
 * @Date: 12-21-2020 21:57
 */
public interface ElasticsearchMapper extends ElasticsearchRepository<ArticleSearchDTO, Integer> {
}
