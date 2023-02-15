package com.star.rpc.utils;

import cn.hutool.extra.spring.SpringUtil;
import com.star.rpc.entity.JobLog;
import com.star.rpc.entity.ScheduleJob;
import com.star.rpc.mapper.JobLogMapper;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.Date;

import static com.star.common.constant.ScheduleConst.*;
import static com.star.common.exception.StarryException.getTrace;

/**
 * 模版方法
 *
 * @Author: Starry
 * @Date: 02-14-2023
 */
public abstract class AbstractQuartzJob implements Job {

    private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

    private static final ThreadLocal<Date> THREAD_LOCAL = new ThreadLocal<>();

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        ScheduleJob job = new ScheduleJob();
        BeanUtils.copyProperties(context.getMergedJobDataMap().get(TASK_PROPERTIES), job);
        try {
            before(context, job);
            if (job != null) {
                doExecute(context, job);
            }
            after(context, job, null);
        } catch (Exception e) {
            log.error("任务执行异常: ", e);
            after(context, job, e);
        }
    }

    /**
     * 记录任务执行开始时间
     *
     * @param context
     * @param job
     */
    protected void before(JobExecutionContext context, ScheduleJob job) {
        THREAD_LOCAL.set(new Date());
    }

    /**
     * 真正执行任务的方法 交给具体的子类实现
     *
     * @param context
     * @param job
     * @throws Exception
     */
    protected abstract void doExecute(JobExecutionContext context, ScheduleJob job) throws Exception;


    /**
     * 将任务执行日志写入数据库中
     *
     * @param context
     * @param job
     * @param e
     */
    protected void after(JobExecutionContext context, ScheduleJob job, Exception e) {
        Date startTime = THREAD_LOCAL.get();
        THREAD_LOCAL.remove();
        final JobLog jobLog = new JobLog();
        jobLog.setJobId(job.getId());
        jobLog.setJobName(job.getJobName());
        jobLog.setJobGroup(job.getJobGroup());
        jobLog.setInvokeTarget(job.getInvokeTarget());
        jobLog.setStartTime(startTime);
        jobLog.setEndTime(new Date());
        long runMs = jobLog.getEndTime().getTime() - jobLog.getStartTime().getTime();
        jobLog.setJobMessage(jobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
        if (e != null) {
            jobLog.setStatus(ZERO);
            jobLog.setExceptionInfo(getTrace(e));
        } else {
            jobLog.setStatus(ONE);
        }
        SpringUtil.getBean(JobLogMapper.class).insert(jobLog);
    }

}
