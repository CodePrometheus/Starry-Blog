package com.star.admin.rest;

import com.star.admin.service.AdminResourceService;
import com.star.common.constant.Result;
import com.star.inf.dto.LabelOptionDTO;
import com.star.inf.dto.ResourceDTO;
import com.star.inf.vo.ConditionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 18:10
 */
@Api(tags = "资源模块")
@RestController
@RequestMapping("admin")
public class AdminResourceController {

    @Resource
    private AdminResourceService adminResourceService;

    @ApiOperation(value = "导入swagger接口")
    @GetMapping("/resources/import")
    public Result<?> importSwagger() {
        adminResourceService.importSwagger();
        return Result.success();
    }

    @ApiOperation(value = "查看资源列表")
    @GetMapping("/resources")
    public Result<List<ResourceDTO>> listResources(ConditionVO condition) {
        return Result.success(adminResourceService.listResources(condition));
    }

    @ApiOperation(value = "删除资源")
    @DeleteMapping("/resources/{resourceId}")
    public Result<?> deleteResource(@PathVariable("resourceId") Integer resourceId) {
        adminResourceService.deleteResource(resourceId);
        return Result.success();
    }

    @ApiOperation(value = "查看角色资源选项")
    @GetMapping("/role/resources")
    public Result<List<LabelOptionDTO>> listResourceOption() {
        return Result.success(adminResourceService.listResourceOption());
    }

}
