package com.star.admin.utils;

import com.star.common.exception.TaskException;
import com.star.admin.entity.ScheduleJob;
import org.quartz.*;

import static com.star.common.constant.ScheduleConst.*;

/**
 * @Author: Starry
 * @Date: 02-14-2023
 */
public class ScheduleUtils {

    private static Class<? extends org.quartz.Job> getQuartzJobClass(ScheduleJob job) {
        boolean isConcurrent = Integer.valueOf(1).equals(job.getConcurrent());
        return isConcurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
    }

    public static TriggerKey getTriggerKey(Integer jobId, String jobGroup) {
        return TriggerKey.triggerKey(TASK_CLASS_NAME + jobId, jobGroup);
    }

    public static JobKey getJobKey(Integer jobId, String jobGroup) {
        return JobKey.jobKey(TASK_CLASS_NAME + jobId, jobGroup);
    }

    public static void createScheduleJob(Scheduler scheduler, ScheduleJob job) throws SchedulerException, TaskException {
        Class<? extends org.quartz.Job> jobClass = getQuartzJobClass(job);
        Integer jobId = job.getId();
        String jobGroup = job.getJobGroup();
        JobDetail jobDetail = JobBuilder.newJob(jobClass).withIdentity(getJobKey(jobId, jobGroup)).build();
        CronScheduleBuilder cronScheduleBuilder = CronScheduleBuilder.cronSchedule(job.getCronExpression());
        cronScheduleBuilder = handleCronScheduleMisfirePolicy(job, cronScheduleBuilder);
        CronTrigger trigger = TriggerBuilder.newTrigger().withIdentity(getTriggerKey(jobId, jobGroup))
                .withSchedule(cronScheduleBuilder).build();
        jobDetail.getJobDataMap().put(TASK_PROPERTIES, job);
        if (scheduler.checkExists(getJobKey(jobId, jobGroup))) {
            scheduler.deleteJob(getJobKey(jobId, jobGroup));
        }
        scheduler.scheduleJob(jobDetail, trigger);
        if (job.getStatus().equals(PAUSE)) {
            scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
        }
    }

    public static CronScheduleBuilder handleCronScheduleMisfirePolicy(ScheduleJob job, CronScheduleBuilder cb)
            throws TaskException {
        switch (job.getMisfirePolicy()) {
            case MISFIRE_DEFAULT:
                return cb;
            case MISFIRE_IGNORE_MISFIRES:
                return cb.withMisfireHandlingInstructionIgnoreMisfires();
            case MISFIRE_FIRE_AND_PROCEED:
                return cb.withMisfireHandlingInstructionFireAndProceed();
            case MISFIRE_DO_NOTHING:
                return cb.withMisfireHandlingInstructionDoNothing();
            default:
                throw new TaskException("The task misfire policy '" + job.getMisfirePolicy()
                        + "' cannot be used in cron schedule tasks", TaskException.Code.CONFIG_ERROR);
        }
    }

}
