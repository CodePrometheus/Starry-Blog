package com.star.common.annotation;

import java.lang.annotation.*;

/**
 * 后台操作日志
 *
 * @Author: zzStar
 * @Date: 06-25-2021 22:18
 */
@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface OptLog {

    /**
     * 操作类型
     *
     * @return /
     */
    String optType() default "";

}
