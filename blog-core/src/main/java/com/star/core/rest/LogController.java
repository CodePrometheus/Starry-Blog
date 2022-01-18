package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.dto.OperationLogDTO;
import com.star.core.dto.PageData;
import com.star.core.dto.VisitLogDTO;
import com.star.core.service.OperationLogService;
import com.star.core.service.VisitLogService;
import com.star.core.vo.ConditionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
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
    private OperationLogService operationLogService;

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
