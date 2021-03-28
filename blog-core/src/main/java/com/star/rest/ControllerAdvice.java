package com.star.rest;


import com.star.constant.Result;
import com.star.constant.StatusConst;
import com.star.exception.StarryException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
    public Result errorHandler(StarryException e) {
        return new Result(false, StatusConst.ERROR, e.getMessage());
    }

    /**
     * 处理参数异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result handleValidException(MethodArgumentNotValidException e) {
        return new Result(false, StatusConst.ERROR, e.getBindingResult().getFieldError().getDefaultMessage());
    }

}
