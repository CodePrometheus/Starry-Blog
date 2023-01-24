package com.star.core.dto;

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
@Document(indexName = "starry-blog")
public class ArticleSearchDTO {

    /**
     * 文章id
     */
    @Id
    // @org.apache.solr.client.solrj.beans.Field("id")
    private String id;

    /**
     * 文章标题
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    // @org.apache.solr.client.solrj.beans.Field("articleTitle")
    private String articleTitle;

    /**
     * 文章内容
     */
    @Field(type = FieldType.Text, analyzer = "ik_max_word")
    // @org.apache.solr.client.solrj.beans.Field("articleContent")
    private String articleContent;

    /**
     * 是否删除
     */
    @Field(type = FieldType.Integer)
    // @org.apache.solr.client.solrj.beans.Field("isDelete")
    private Integer isDelete;

    /**
     * 文章状态
     */
    @Field(type = FieldType.Integer)
    // @org.apache.solr.client.solrj.beans.Field("status")
    private Integer status;

}
