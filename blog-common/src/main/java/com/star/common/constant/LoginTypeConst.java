package com.star.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 登录的方式
 *
 * @Author: zzStar
 * @Date: 12-21-2020 16:23
 */
@Getter
@AllArgsConstructor
public enum LoginTypeConst {

    /**
     * 邮箱登录
     */
    EMAIL(0, "邮箱登录"),

    /**
     * QQ登录
     */
    QQ(1, "QQ登录"),

    /**
     * 微博登录
     */
    WEIBO(2, "微博登录");

    /**
     * 登录方式
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

}
