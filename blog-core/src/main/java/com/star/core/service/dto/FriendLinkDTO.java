package com.star.core.service.dto;

import lombok.Data;

/**
 * 友链列表
 *
 * @Author: zzStar
 * @Date: 12-19-2020 23:40
 */
@Data
public class FriendLinkDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 链接名
     */
    private String linkName;

    /**
     * 链接头像
     */
    private String linkAvatar;

    /**
     * 链接地址
     */
    private String linkAddress;

    /**
     * 介绍
     */
    private String linkIntro;
}
