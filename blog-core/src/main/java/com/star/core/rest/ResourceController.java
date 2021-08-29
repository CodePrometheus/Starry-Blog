package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.dto.LabelOptionDTO;
import com.star.core.dto.ResourceDTO;
import com.star.core.service.ResourceService;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.ResourceVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 18:10
 */
@Api(tags = "资源模块")
@RestController
@RequestMapping("admin")
public class ResourceController {

    @Resource
    private ResourceService resourceService;

    @ApiOperation(value = "导入swagger接口")
    @GetMapping("/resources/import")
    public Result<?> importSwagger() {
        resourceService.importSwagger();
        return Result.success();
    }

    @ApiOperation(value = "查看资源列表")
    @GetMapping("/resources")
    public Result<List<ResourceDTO>> listResources(ConditionVO condition) {
        return Result.success(resourceService.listResources(condition));
    }

    @ApiOperation(value = "删除资源")
    @DeleteMapping("/resources/{resourceId}")
    public Result<?> deleteResource(@PathVariable("resourceId") Integer resourceId) {
        resourceService.deleteResource(resourceId);
        return Result.success();
    }

    @ApiOperation(value = "新增或修改资源")
    @PostMapping("/resources")
    public Result<?> saveOrUpdateResources(@RequestBody @Valid ResourceVO resourceVO) {
        resourceService.saveOrUpdateResource(resourceVO);
        return Result.success();
    }

    @ApiOperation(value = "查看角色资源选项")
    @GetMapping("/role/resources")
    public Result<List<LabelOptionDTO>> listResourceOption() {
        return Result.success(resourceService.listResourceOption());
    }

}
