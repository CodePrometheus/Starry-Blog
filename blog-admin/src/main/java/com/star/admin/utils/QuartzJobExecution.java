package com.star.admin.utils;

import com.star.admin.entity.ScheduleJob;
import org.quartz.JobExecutionContext;

/**
 * 任务异常
 *
 * @Author: Starry
 * @Date: 02-16-2023
 */
public class QuartzJobExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(JobExecutionContext context, ScheduleJob job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }

}
