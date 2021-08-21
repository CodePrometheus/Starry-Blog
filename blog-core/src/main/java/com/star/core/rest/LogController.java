package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.dto.OperationLogDTO;
import com.star.core.dto.PageData;
import com.star.core.service.OperationLogService;
import com.star.core.vo.ConditionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 2021/8/21
 * @Description:
 */
@Api(tags = "日志模块")
@RestController
public class LogController {

    @Resource
    private OperationLogService operationLogService;

    @ApiOperation(value = "查看操作日志")
    @GetMapping("/admin/operation")
    public Result<PageData<OperationLogDTO>> listOperationLogs(ConditionVO condition) {
        return Result.success(operationLogService.listOperationLogs(condition));
    }

    @ApiOperation(value = "删除操作日志")
    @DeleteMapping("/admin/operation")
    public Result<?> deleteOperationLogs(@RequestBody List<Integer> logList) {
        operationLogService.removeByIds(logList);
        return Result.success();
    }

}
