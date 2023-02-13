package com.star.admin.rest;

import com.star.admin.entity.ExceptionLog;
import com.star.admin.service.AdminExceptionService;
import com.star.common.constant.Result;
import com.star.inf.dto.PageData;
import com.star.inf.vo.ConditionVO;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Starry
 * @Date: 02-02-2023
 */
@RestController
@RequestMapping("admin")
public class AdminExceptionController {

    @Resource
    private AdminExceptionService adminExceptionService;

    @ApiOperation("获取异常日志")
    @GetMapping("/exception/logs")
    public Result<PageData<ExceptionLog>> getExceptionLogs(ConditionVO conditionVO) {
        return Result.success(adminExceptionService.getExceptionLogs(conditionVO));
    }

    @ApiOperation(value = "删除异常日志")
    @DeleteMapping("/exception/logs")
    public Result<?> deleteExceptionLogs(@RequestBody List<Integer> logIds) {
        adminExceptionService.removeByIds(logIds);
        return Result.success();
    }

}
