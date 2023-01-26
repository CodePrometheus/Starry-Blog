package com.star.inf.mapper;

import com.star.inf.dto.ArticleSearchDTO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

/**
 * @Description: es
 * @Author: zzStar
 * @Date: 12-21-2020 21:57
 */
@Mapper
public interface ElasticsearchMapper extends ElasticsearchRepository<ArticleSearchDTO, Integer> {
}
