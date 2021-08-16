package com.star.common.exception;

import com.star.common.constant.StatusConst;
import lombok.AllArgsConstructor;
import lombok.Getter;

import static com.star.common.constant.StatusConst.FAIL;

/**
 * 自定义异常类
 *
 * @Author: zzStar
 * @Date: 12-19-2020 15:25
 */
@Getter
@AllArgsConstructor
public class StarryException extends RuntimeException {

    /**
     * 错误码
     */
    private Integer code = FAIL.getCode();

    /**
     * 错误信息
     */
    private final String message;

    public StarryException(String message) {
        this.message = message;
    }

    public StarryException(StatusConst statusConst) {
        this.code = statusConst.getCode();
        this.message = statusConst.getDesc();
    }

}
