package com.star.core.handler;

import com.star.common.constant.Result;
import com.star.common.exception.StarryException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

import static com.star.common.constant.StatusConst.SYSTEM_ERROR;
import static com.star.common.constant.StatusConst.VALID_ERROR;

/**
 * 全局异常处理
 *
 * @Author: zzStar
 * @Date: 12-20-2020 17:30
 */
@RestControllerAdvice
public class ControllerAdvice {

    /**
     * 处理服务异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = StarryException.class)
    public Result<?> errorHandler(StarryException e) {
        return Result.fail(e.getCode(), e.getMessage());
    }

    /**
     * 处理参数校验异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidException(MethodArgumentNotValidException e) {
        return Result.fail(VALID_ERROR.getCode(), Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

    /**
     * 处理系统异常
     *
     * @param e 异常
     * @return 接口异常信息
     */
    @ExceptionHandler(value = Exception.class)
    public Result<?> systemException(Exception e) {
        e.printStackTrace();
        return Result.fail(SYSTEM_ERROR.getCode(), SYSTEM_ERROR.getDesc());
    }

}
