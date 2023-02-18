package com.star.admin.rest;

import com.star.admin.domain.dto.JobDTO;
import com.star.admin.domain.vo.JobRunVO;
import com.star.admin.domain.vo.JobSearchVO;
import com.star.admin.domain.vo.JobStatusVO;
import com.star.admin.domain.vo.JobVO;
import com.star.admin.service.AdminJobService;
import com.star.common.constant.Result;
import com.star.inf.dto.PageData;
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
public class AdminJobController {

    @Resource
    private AdminJobService adminJobService;

    @ApiOperation("添加定时任务")
    @PostMapping("/jobs")
    public Result<?> saveJob(@RequestBody JobVO jobVO) {
        adminJobService.saveJob(jobVO);
        return Result.success();
    }

    @ApiOperation("修改定时任务")
    @PutMapping("/jobs")
    public Result<?> updateJob(@RequestBody JobVO jobVO) {
        adminJobService.updateJob(jobVO);
        return Result.success();
    }

    @ApiOperation("删除定时任务")
    @DeleteMapping("/jobs")
    public Result<?> deleteJobById(@RequestBody List<Integer> jobIds) {
        adminJobService.deleteJobs(jobIds);
        return Result.success();
    }

    @ApiOperation("根据id获取任务")
    @GetMapping("/jobs/{id}")
    public Result<JobDTO> getJobById(@PathVariable("id") Integer jobId) {
        return Result.success(adminJobService.getJobById(jobId));
    }

    @ApiOperation("获取任务列表")
    @GetMapping("/jobs")
    public Result<PageData<JobDTO>> listJobs(JobSearchVO jobSearchVO) {
        return Result.success(adminJobService.listJobs(jobSearchVO));
    }

    @ApiOperation("更改任务的状态")
    @PutMapping("/jobs/status")
    public Result<?> updateJobStatus(@RequestBody JobStatusVO jobStatusVO) {
        adminJobService.updateJobStatus(jobStatusVO);
        return Result.success();
    }

    @ApiOperation("执行某个任务")
    @PutMapping("/jobs/run")
    public Result<?> runJob(@RequestBody JobRunVO jobRunVO) {
        adminJobService.runJob(jobRunVO);
        return Result.success();
    }

    @ApiOperation("获取所有job分组")
    @GetMapping("/jobs/jobGroups")
    public Result<List<String>> listJobGroup() {
        return Result.success(adminJobService.listJobGroups());
    }

}
