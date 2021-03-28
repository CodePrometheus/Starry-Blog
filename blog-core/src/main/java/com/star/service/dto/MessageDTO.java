package com.star.service.dto;

import lombok.Data;

/**
 * 留言
 *
 * @Author: zzStar
 * @Date: 12-20-2020 10:29
 */
@Data
public class MessageDTO {

    /**
     * 主键id
     */
    private Integer id;

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
     * 弹幕速度
     */
    private Integer time;

}
