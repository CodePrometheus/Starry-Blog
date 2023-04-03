package com.star.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.admin.domain.dto.JobLogDTO;
import com.star.admin.domain.vo.JobLogSearchVO;
import com.star.admin.entity.JobLog;
import com.star.admin.mapper.JobLogMapper;
import com.star.common.tool.BeanCopyUtils;
import com.star.common.tool.PageUtils;
import com.star.inf.dto.PageData;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @Author: Starry
 * @Date: 02-19-2023
 */
@Service
public class AdminJobLogService extends ServiceImpl<JobLogMapper, JobLog> {

    @Resource
    private JobLogMapper jobLogMapper;

    /**
     * 获取定时任务的日志列表
     *
     * @param jobLogSearchVO
     * @return
     */
    public PageData<JobLogDTO> listJobLogs(JobLogSearchVO jobLogSearchVO) {
        // JobId | JobName | JobGroup | JobStatus | CreateTime
        Page<JobLog> page = new Page<>(PageUtils.getCurrent(), PageUtils.getSize());
        Page<JobLog> jobLogPage = jobLogMapper.selectPage(page, new LambdaQueryWrapper<JobLog>()
                .orderByDesc(JobLog::getCreateTime)
                .eq(Objects.nonNull(jobLogSearchVO.getJobId()), JobLog::getJobId, jobLogSearchVO.getJobId())
                .eq(Objects.nonNull(jobLogSearchVO.getStatus()), JobLog::getStatus, jobLogSearchVO.getStatus())
                .like(StringUtils.isNotBlank(jobLogSearchVO.getJobName()), JobLog::getJobName, jobLogSearchVO.getJobName())
                .like(StringUtils.isNotBlank(jobLogSearchVO.getJobGroup()), JobLog::getJobGroup, jobLogSearchVO.getJobGroup())
                .between(Objects.nonNull(jobLogSearchVO.getStartTime()) && Objects.nonNull(jobLogSearchVO.getEndTime()),
                        JobLog::getCreateTime, jobLogSearchVO.getStartTime(), jobLogSearchVO.getEndTime()));
        List<JobLogDTO> jobLogDTOList = BeanCopyUtils.copyList(jobLogPage.getRecords(), JobLogDTO.class);
        return new PageData<>(jobLogDTOList, jobLogPage.getTotal());
    }

    /**
     * 删除定时任务的日志
     *
     * @param ids
     */
    @Transactional(rollbackFor = Exception.class)
    public void deleteJobLogs(List<Integer> ids) {
        jobLogMapper.delete(new LambdaQueryWrapper<JobLog>().in(JobLog::getId, ids));
    }

    /**
     * 清除定时任务的日志
     */
    @Transactional(rollbackFor = Exception.class)
    public void cleanJobLogs() {
        jobLogMapper.delete(null);
    }

    /**
     * 获取定时任务日志的所有组名
     *
     * @return
     */
    public List<String> listJobLogGroups() {
        return jobLogMapper.selectList(new LambdaQueryWrapper<JobLog>()
                        .select(JobLog::getJobGroup)).stream()
                .map(JobLog::getJobGroup)
                .distinct()
                .collect(Collectors.toList());
    }

}
