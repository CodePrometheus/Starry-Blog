package com.star.admin.rest;

import com.star.admin.service.AdminOperationLogService;
import com.star.admin.service.VisitLogService;
import com.star.common.constant.Result;
import com.star.inf.dto.OperationLogDTO;
import com.star.inf.dto.PageData;
import com.star.inf.dto.VisitLogDTO;
import com.star.inf.vo.ConditionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 2021/8/21
 * @Description:
 */
@Api(tags = "日志模块")
@RestController
@RequestMapping("admin")
public class LogController {

    @Resource
    private VisitLogService visitLogService;

    @Resource
    private AdminOperationLogService operationLogService;

    @ApiOperation(value = "查看操作日志")
    @GetMapping("/operation")
    public Result<PageData<OperationLogDTO>> listOperationLogs(ConditionVO condition) {
        return Result.success(operationLogService.listOperationLogs(condition));
    }

    @ApiOperation(value = "删除操作日志")
    @DeleteMapping("/operation")
    public Result<?> deleteOperationLogs(@RequestBody List<Integer> logList) {
        operationLogService.removeByIds(logList);
        return Result.success();
    }

    @ApiOperation(value = "查看操作日志")
    @GetMapping("/visit")
    public Result<PageData<VisitLogDTO>> listVisitLogs(ConditionVO condition) {
        return Result.success(visitLogService.listVisitLogs(condition));
    }

    @ApiOperation(value = "删除操作日志")
    @DeleteMapping("/visit")
    public Result<?> deleteVisitLogs(@RequestBody List<Integer> logList) {
        visitLogService.removeByIds(logList);
        return Result.success();
    }

}
