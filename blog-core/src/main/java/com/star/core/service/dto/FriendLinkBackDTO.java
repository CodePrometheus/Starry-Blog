package com.star.core.service.dto;

import lombok.Data;

import java.util.Date;

/**
 * 后台友链列表
 *
 * @Author: zzStar
 * @Date: 12-19-2020 23:42
 */
@Data
public class FriendLinkBackDTO {
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
     * 链接介绍
     */
    private String linkIntro;

    /**
     * 创建时间
     */
    private Date createTime;

}
