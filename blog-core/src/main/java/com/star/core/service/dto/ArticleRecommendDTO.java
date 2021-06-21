package com.star.core.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * 推荐文章
 *
 * @Author: zzStar
 * @Date: 03-30-2021 16:33
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ArticleRecommendDTO {

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
     * 创建时间
     */
    private Date createTime;

}
