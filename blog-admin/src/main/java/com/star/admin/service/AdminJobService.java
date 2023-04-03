package com.star.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.admin.domain.dto.JobDTO;
import com.star.admin.domain.vo.JobRunVO;
import com.star.admin.domain.vo.JobStatusVO;
import com.star.admin.domain.vo.JobVO;
import com.star.admin.entity.JobLog;
import com.star.admin.entity.ScheduleJob;
import com.star.admin.mapper.JobMapper;
import com.star.admin.utils.CronUtils;
import com.star.admin.utils.ScheduleUtils;
import com.star.common.constant.ScheduleConst;
import com.star.common.tool.BeanCopyUtils;
import com.star.common.tool.PageUtils;
import com.star.inf.dto.PageData;
import com.star.inf.vo.ConditionVO;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: Starry
 * @Date: 02-14-2023
 */
@Service
public class AdminJobService extends ServiceImpl<JobMapper, ScheduleJob> {

    private static final Logger log = LoggerFactory.getLogger(AdminJobService.class);

    @Resource
    private Scheduler scheduler;

    @Resource
    private JobMapper jobMapper;

    @PostConstruct
    public void init() {
        try {
            scheduler.clear();
            List<ScheduleJob> jobsAll = jobMapper.selectList(null);
            jobsAll.forEach(scheduleJob -> {
                try {
                    ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
                } catch (Exception e) {
                    log.error("JobService.createScheduleJob failed: ", e);
                }
            });
        } catch (Exception e) {
            log.error("JobService.init failed: ", e);
        }
    }

    private void checkCronIsValid(JobVO jobVO) {
        boolean valid = CronUtils.isValid(jobVO.getCronExpression());
        Assert.isTrue(valid, "Cron表达式无效!");
    }

    /**
     * 添加定时任务
     *
     * @param jobVO
     */
    @SneakyThrows
    @Transactional(rollbackFor = Exception.class)
    public void saveJob(JobVO jobVO) {
        checkCronIsValid(jobVO);
        ScheduleJob scheduleJob = BeanCopyUtils.copyObject(jobVO, ScheduleJob.class);
        int insert = jobMapper.insert(scheduleJob);
        if (insert > 0) {
            ScheduleUtils.createScheduleJob(scheduler, scheduleJob);
        }
    }

    /**
     * 修改定时任务
     *
     * @param jobVO
     */
    @Transactional(rollbackFor = Exception.class)
    public void updateJob(JobVO jobVO) {
        checkCronIsValid(jobVO);
        ScheduleJob jobInfo = jobMapper.selectById(jobVO.getId());
        ScheduleJob job = BeanCopyUtils.copyObject(jobVO, ScheduleJob.class);
        int update = jobMapper.updateById(job);
        if (update > 0) {
            updateSchedulerJob(job, jobInfo.getJobGroup());
        }
    }

    @SneakyThrows
    private void updateSchedulerJob(ScheduleJob job, String jobGroup) {
        Integer jobId = job.getId();
        JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobGroup);
        if (scheduler.checkExists(jobKey)) {
            scheduler.deleteJob(jobKey);
        }
        ScheduleUtils.createScheduleJob(scheduler, job);
    }

    /**
     * 删除定时任务
     *
     * @param jobIds
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobs(List<Integer> jobIds) {
        List<ScheduleJob> jobs = jobMapper.selectList(new LambdaQueryWrapper<ScheduleJob>().in(ScheduleJob::getId, jobIds));
        int delete = jobMapper.delete(new LambdaQueryWrapper<ScheduleJob>().in(ScheduleJob::getId, jobIds));
        if (delete > 0) {
            jobs.forEach(job -> {
                try {
                    scheduler.deleteJob(ScheduleUtils.getJobKey(job.getId(), job.getJobGroup()));
                } catch (SchedulerException e) {
                    log.error("JobService.deleteJobs failed: ", e);
                }
            });
        }
    }

    /**
     * 根据id获取任务
     *
     * @param jobId
     * @return
     */
    public JobDTO getJobById(Integer jobId) {
        ScheduleJob scheduleJob = jobMapper.selectById(jobId);
        JobDTO jobDTO = BeanCopyUtils.copyObject(scheduleJob, JobDTO.class);
        jobDTO.setNextValidTime(CronUtils.getNextExecution(jobDTO.getCronExpression()));
        return jobDTO;
    }

    /**
     * 获取任务列表
     *
     * @param conditionVO
     * @return
     */
    @SneakyThrows
    public PageData<JobDTO> listJobs(ConditionVO conditionVO) {
        Page<ScheduleJob> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        // JobName || JobGroup || JobStatus
        Page<ScheduleJob> jobPage = this.page(page, new LambdaQueryWrapper<ScheduleJob>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()), ScheduleJob::getJobName, conditionVO.getKeywords())
                .eq(StringUtils.isNotBlank(conditionVO.getKeywords()), ScheduleJob::getJobGroup, conditionVO.getKeywords())
                .eq(Objects.nonNull(conditionVO.getStatus()), ScheduleJob::getStatus, conditionVO.getStatus())
                .orderByDesc(ScheduleJob::getCreateTime)
        );
        List<JobDTO> jobDTOList = BeanCopyUtils.copyList(jobPage.getRecords(), JobDTO.class);
        return new PageData<>(jobDTOList, jobPage.getTotal());
    }

    /**
     * 更改任务的状态
     *
     * @param jobStatusVO
     */
    @SneakyThrows
    public void updateJobStatus(JobStatusVO jobStatusVO) {
        ScheduleJob scheduleJob = jobMapper.selectById(jobStatusVO.getId());
        if (scheduleJob.getStatus().equals(jobStatusVO.getStatus())) {
            return;
        }
        Integer jobStatus = jobStatusVO.getStatus();
        int update = jobMapper.update(null, new LambdaUpdateWrapper<ScheduleJob>()
                .eq(ScheduleJob::getId, jobStatusVO.getId())
                .set(ScheduleJob::getStatus, jobStatus));

        Integer jobId = scheduleJob.getId();
        String jobGroup = scheduleJob.getJobGroup();
        if (update > 0) {
            if (ScheduleConst.NORMAL == jobStatus) {
                scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobGroup));
            } else if (ScheduleConst.PAUSE == jobStatus) {
                scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobGroup));
            }
        }
    }

    /**
     * 执行某个任务
     *
     * @param jobRunVO
     */
    @SneakyThrows
    public void runJob(JobRunVO jobRunVO) {
        System.out.println("getId() = " + jobRunVO.getId() + " getJobGroup(): " + jobRunVO.getJobGroup());
        System.out.println("runJob = " + ScheduleUtils.getJobKey(jobRunVO.getId(), jobRunVO.getJobGroup()));
        scheduler.triggerJob(ScheduleUtils.getJobKey(jobRunVO.getId(), jobRunVO.getJobGroup()));
    }

    /**
     * 获取所有job分组
     *
     * @return
     */
    public List<String> listJobGroups() {
        return jobMapper.selectList(new LambdaQueryWrapper<ScheduleJob>()
                        .select(ScheduleJob::getJobGroup)).stream()
                .map(ScheduleJob::getJobGroup)
                .distinct()
                .collect(Collectors.toList());
    }

}
