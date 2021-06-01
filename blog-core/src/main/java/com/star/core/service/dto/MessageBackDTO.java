package com.star.core.service.dto;

import lombok.Data;

import java.util.Date;

/**
 * 后台留言列表
 *
 * @Author: zzStar
 * @Date: 12-20-2020 10:54
 */
@Data
public class MessageBackDTO {

    /**
     * 主键id
     */
    private Integer id;

    /**
     * 用户ip
     */
    private String ipAddress;

    /**
     * 用户ip地址
     */
    private String ipSource;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 留言内容
     */
    private String messageContent;

    /**
     * 留言时间
     */
    private Date createTime;

}
