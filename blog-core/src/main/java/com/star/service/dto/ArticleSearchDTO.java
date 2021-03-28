package com.star.service.dto;

import com.star.domain.entity.Article;
import lombok.Data;
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
@Document(indexName = "article", type = "_doc")
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

    public ArticleSearchDTO(Article article) {
        this.id = article.getId();
        this.articleTitle = article.getArticleTitle();
        this.articleContent = article.getArticleContent();
        this.isDelete = article.getIsDelete();
    }

    public ArticleSearchDTO() {
    }

}
