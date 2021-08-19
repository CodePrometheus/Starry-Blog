package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

/**
 * 文章排行VO
 *
 * @Author: zzStar
 * @Date: 12-19-2020 21:53
 */
@Data
@Builder
@AllArgsConstructor
public class ArticleRankDTO {

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 浏览量
     */
    private Integer viewsCount;

}