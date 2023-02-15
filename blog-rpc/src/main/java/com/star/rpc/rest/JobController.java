package com.star.rpc.rest;

import com.star.common.constant.Result;
import com.star.inf.dto.PageData;
import com.star.rpc.domain.dto.JobDTO;
import com.star.rpc.domain.vo.JobRunVO;
import com.star.rpc.domain.vo.JobSearchVO;
import com.star.rpc.domain.vo.JobStatusVO;
import com.star.rpc.domain.vo.JobVO;
import com.star.rpc.service.JobService;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Starry
 * @Date: 02-15-2023
 */
@RestController
@RequestMapping("admin")
public class JobController {

    @Resource
    private JobService jobService;

    @ApiOperation("添加定时任务")
    @PostMapping("/jobs")
    public Result<?> saveJob(@RequestBody JobVO jobVO) {
        jobService.saveJob(jobVO);
        return Result.success();
    }

    @ApiOperation("修改定时任务")
    @PutMapping("/jobs")
    public Result<?> updateJob(@RequestBody JobVO jobVO) {
        jobService.updateJob(jobVO);
        return Result.success();
    }

    @ApiOperation("删除定时任务")
    @DeleteMapping("/jobs")
    public Result<?> deleteJobById(@RequestBody List<Integer> jobIds) {
        jobService.deleteJobs(jobIds);
        return Result.success();
    }

    @ApiOperation("根据id获取任务")
    @GetMapping("/jobs/{id}")
    public Result<JobDTO> getJobById(@PathVariable("id") Integer jobId) {
        return Result.success(jobService.getJobById(jobId));
    }

    @ApiOperation("获取任务列表")
    @GetMapping("/jobs")
    public Result<PageData<JobDTO>> listJobs(JobSearchVO jobSearchVO) {
        return Result.success(jobService.listJobs(jobSearchVO));
    }

    @ApiOperation("更改任务的状态")
    @PutMapping("/jobs/status")
    public Result<?> updateJobStatus(@RequestBody JobStatusVO jobStatusVO) {
        jobService.updateJobStatus(jobStatusVO);
        return Result.success();
    }

    @ApiOperation("执行某个任务")
    @PutMapping("/jobs/run")
    public Result<?> runJob(@RequestBody JobRunVO jobRunVO) {
        jobService.runJob(jobRunVO);
        return Result.success();
    }

    @ApiOperation("获取所有job分组")
    @GetMapping("/jobs/jobGroups")
    public Result<List<String>> listJobGroup() {
        return Result.success(jobService.listJobGroups());
    }

}
