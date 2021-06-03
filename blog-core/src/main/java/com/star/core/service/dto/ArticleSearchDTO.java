package com.star.core.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * 文章搜索结果
 *
 * @Author: zzStar
 * @Date: 12-19-2020 14:14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName = "starry-article", replicas = 0)
public class ArticleSearchDTO {

    /**
     * 文章id
     */
    @Id
    private Integer id;

    /**
     * 文章标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String articleTitle;

    /**
     * 文章内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    private String articleContent;

    /**
     * 文章状态
     */
    @Field(type = FieldType.Integer)
    private Integer isDelete;

}
