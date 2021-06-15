package com.star.core.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 前台博客信息DTO
 *
 * @Author: zzStar
 * @Date: 12-19-2020 19:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogHomeInfoDTO {

    /**
     * 博主昵称
     */
    private String nickname;

    /**
     * 博主头像
     */
    private String avatar;

    /**
     * 博主简介
     */
    private String intro;

    /**
     * 文章数量
     */
    private Integer articleCount;

    /**
     * 分类数量
     */
    private Integer categoryCount;

    /**
     * 标签数量
     */
    private Integer tagCount;

    /**
     * 公告
     */
    private String notice;

    /**
     * 访问量
     */
    private String viewsCount;

}
