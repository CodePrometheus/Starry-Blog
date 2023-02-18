package com.star.admin.utils;

import com.star.admin.entity.ScheduleJob;
import org.quartz.JobExecutionContext;

/**
 * 避免同一作业的重叠执行
 * 不允许并发执行
 *
 * @Author: Starry
 * @Date: 02-16-2023
 */
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {

    @Override
    protected void doExecute(JobExecutionContext context, ScheduleJob job) throws Exception {
        JobInvokeUtil.invokeMethod(job);
    }

}
