package com.star.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 返回状态码
 *
 * @Author: zzStar
 * @Date: 12-16-2020 22:22
 */
@Getter
@AllArgsConstructor
public enum StatusConst {
    /**
     * 成功
     */
    SUCCESS(2000, "操作成功"),
    /**
     * 没有操作权限
     */
    AUTHORIZED(4030, "没有操作权限"),
    /**
     * 系统异常
     */
    SYSTEM_ERROR(5000, "系统异常"),
    /**
     * 失败
     */
    FAIL(5100, "操作失败"),
    /**
     * 参数校验失败
     */
    VALID_ERROR(5200, "参数格式不正确"),
    /**
     * 用户名已存在
     */
    USERNAME_EXIST(5201, "用户名已存在"),
    /**
     * 用户名不存在
     */
    USERNAME_NOT_EXIST(5202, "用户名不存在");

    /**
     * 状态码
     */
    private final Integer code;

    /**
     * 描述
     */
    private final String desc;


}
