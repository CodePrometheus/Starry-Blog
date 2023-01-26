package com.star.inf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 友链列表
 *
 * @Author: zzStar
 * @Date: 12-19-2020 23:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
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
