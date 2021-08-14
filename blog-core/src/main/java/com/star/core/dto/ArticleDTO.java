package com.star.core.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 文章
 *
 * @Author: zzStar
 * @Date: 12-19-2020 15:15
 */
@Data
public class ArticleDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 文章缩略图
     */
    private String articleCover;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 内容
     */
    private String articleContent;

    /**
     * 点赞量
     */
    private Integer likeCount;

    /**
     * 浏览量
     */
    private Integer viewsCount;

    /**
     * 发表时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 文章分类id
     */
    private Integer categoryId;

    /**
     * 文章分类名
     */
    private String categoryName;

    /**
     * 文章标签
     */
    private List<TagDTO> tagDTOList;

    /**
     * 上一篇文章
     */
    private ArticlePaginationDTO lastArticle;

    /**
     * 下一篇文章
     */
    private ArticlePaginationDTO nextArticle;

    /**
     * 推荐文章
     */
    private List<ArticleRecommendDTO> articleRecommendList;

}
