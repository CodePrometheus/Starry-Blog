package com.star.core.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 分类或标签下的文章
 *
 * @Author: zzStar
 * @Date: 12-19-2020 13:59
 */
@Data
public class ArticlePreviewDTO {

    /**
     * 文章id
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
     * 发表时间
     */
    private Date createTime;

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
    private List<TagDTO> tagList;


}
