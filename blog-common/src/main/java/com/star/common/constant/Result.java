package com.star.common.constant;

import lombok.Data;

import java.io.Serializable;

import static com.star.common.constant.StatusConst.FAIL;
import static com.star.common.constant.StatusConst.SUCCESS;

/**
 * 定义返回结果集
 *
 * @Author: zzStar
 * @Date: 12-16-2020 22:16
 */
@Data
@SuppressWarnings("unchecked")
public class Result<T> implements Serializable {

    /**
     * 返回状态
     */
    private Boolean flag;
    /**
     * 返回码
     */
    private Integer code;
    /**
     * 返回信息
     */
    private String message;
    /**
     * 返回数据
     */
    private T data;

    public static <T> Result<T> success() {
        return restResult(true, null, SUCCESS.getCode(), SUCCESS.getDesc());
    }

    public static <T> Result<T> success(T data) {
        return restResult(true, data, SUCCESS.getCode(), SUCCESS.getDesc());
    }

    public static <T> Result<T> success(T data, String message) {
        return restResult(true, data, SUCCESS.getCode(), message);
    }

    public static <T> Result<T> fail() {
        return restResult(false, null, FAIL.getCode(), FAIL.getDesc());
    }

    public static <T> Result<T> fail(StatusConst statusConst) {
        return restResult(false, null, statusConst.getCode(), statusConst.getDesc());
    }

    public static <T> Result<T> fail(String message) {
        return restResult(false, message);
    }

    public static <T> Result<T> fail(T data) {
        return restResult(false, data, FAIL.getCode(), FAIL.getDesc());
    }

    public static <T> Result<T> fail(T data, String message) {
        return restResult(false, data, FAIL.getCode(), message);
    }

    public static <T> Result<T> fail(Integer code, String message) {
        return restResult(false, null, code, message);
    }

    private static <T> Result<T> restResult(Boolean flag, String message) {
        Result<T> apiResult = new Result<>();
        apiResult.setFlag(flag);
        apiResult.setCode(flag ? SUCCESS.getCode() : FAIL.getCode());
        apiResult.setMessage(message);
        return apiResult;
    }

    private static <T> Result<T> restResult(Boolean flag, T data, Integer code, String message) {
        Result<T> apiResult = new Result<>();
        apiResult.setFlag(flag);
        apiResult.setData(data);
        apiResult.setCode(code);
        apiResult.setMessage(message);
        return apiResult;
    }

}
