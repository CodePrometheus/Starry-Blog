package com.star.admin.rest;

import com.star.admin.domain.dto.JobLogDTO;
import com.star.admin.domain.vo.JobLogSearchVO;
import com.star.admin.service.AdminJobLogService;
import com.star.common.constant.Result;
import com.star.inf.dto.PageData;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Starry
 * @Date: 02-19-2023
 */
@Api(tags = "定时任务日志模块")
@RestController
@RequestMapping("admin")
public class AdminJobLogController {

    @Resource
    private AdminJobLogService adminJobLogService;

    @ApiOperation("获取定时任务的日志列表")
    @GetMapping("/jobLogs")
    public Result<PageData<JobLogDTO>> listJobLogs(JobLogSearchVO jobLogSearchVO) {
        return Result.success(adminJobLogService.listJobLogs(jobLogSearchVO));
    }

    @ApiOperation("删除定时任务的日志")
    @DeleteMapping("/jobLogs")
    public Result<?> deleteJobLogs(@RequestBody List<Integer> ids) {
        adminJobLogService.deleteJobLogs(ids);
        return Result.success();
    }

    @ApiOperation("清除定时任务的日志")
    @DeleteMapping("/jobLogs/clean")
    public Result<?> cleanJobLogs() {
        adminJobLogService.cleanJobLogs();
        return Result.success();
    }

    @ApiOperation("获取定时任务日志的所有组名")
    @GetMapping("/jobLogs/jobGroups")
    public Result<?> listJobLogGroups() {
        return Result.success(adminJobLogService.listJobLogGroups());
    }

}
