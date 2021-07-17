package com.star.common.constant;

import lombok.Data;

import java.io.Serializable;

/**
 * 定义返回结果集
 *
 * @Author: zzStar
 * @Date: 12-16-2020 22:16
 */
@Data
@SuppressWarnings("unchecked")
public class Result<T> implements Serializable {

    private boolean flag;
    private Integer code;
    private String message;
    private T data;

    public Result(boolean flag, Integer code, String message, Object data) {
        this.flag = flag;
        this.code = code;
        this.message = message;
        this.data = (T) data;
    }

    public Result(boolean flag, Integer code, String message) {
        this.flag = flag;
        this.code = code;
        this.message = message;
    }

    public Result() {
        this.flag = true;
        this.code = StatusConst.OK;
        this.message = "操作成功!";
    }

}
