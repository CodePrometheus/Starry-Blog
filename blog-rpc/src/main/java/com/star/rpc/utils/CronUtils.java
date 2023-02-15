package com.star.rpc.utils;

import org.quartz.CronExpression;

import java.util.Date;

/**
 * Cron 表达式校验
 *
 * @Author: Starry
 * @Date: 02-14-2023
 */
public class CronUtils {

    public static boolean isValid(String cronExpression) {
        return CronExpression.isValidExpression(cronExpression);
    }

    public static String getInvalidMessage(String cronExpression) {
        try {
            new CronExpression(cronExpression);
            return null;
        } catch (Exception pe) {
            return pe.getMessage();
        }
    }

    public static Date getNextExecution(String cronExpression) {
        try {
            CronExpression cron = new CronExpression(cronExpression);
            return cron.getNextValidTimeAfter(new Date(System.currentTimeMillis()));
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }
    }

}
