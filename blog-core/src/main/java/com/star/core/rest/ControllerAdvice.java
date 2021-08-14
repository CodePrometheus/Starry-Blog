package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.common.exception.StarryException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

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
        return Result.success(e.getMessage());
    }

    /**
     * 处理参数异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<?> handleValidException(MethodArgumentNotValidException e) {
        return Result.success(Objects.requireNonNull(e.getBindingResult().getFieldError()).getDefaultMessage());
    }

}
