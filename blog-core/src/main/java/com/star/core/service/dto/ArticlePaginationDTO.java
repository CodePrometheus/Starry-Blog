package com.star.core.service.dto;

import lombok.Data;

/**
 * 文章上下篇
 *
 * @Author: zzStar
 * @Date: 03-30-2021 16:51
 */
@Data
public class ArticlePaginationDTO {

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

}
