package com.star.admin.entity;

import org.springframework.context.ApplicationEvent;

/**
 * @Author: Starry
 * @Date: 02-02-2023
 */
public class ExceptionLogEvent extends ApplicationEvent {

    public ExceptionLogEvent(ExceptionLog exceptionLog) {
        super(exceptionLog);
    }

}
