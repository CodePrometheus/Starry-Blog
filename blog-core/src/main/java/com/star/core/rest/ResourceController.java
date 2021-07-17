package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.domain.vo.ResourceVO;
import com.star.core.service.ResourceService;
import com.star.core.service.dto.LabelOptionDTO;
import com.star.core.service.dto.ResourceDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.star.common.constant.MessageConst.*;
import static com.star.common.constant.StatusConst.OK;

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
        return new Result<>(true, OK, "导入Swagger成功");
    }

    @ApiOperation(value = "查看资源列表")
    @GetMapping("/resources")
    public Result<List<ResourceDTO>> listResources() {
        return new Result<>(true, OK, QUERY, resourceService.listResources());
    }

    @ApiOperation(value = "删除资源")
    @DeleteMapping("/resources")
    public Result<?> deleteResources(@RequestBody List<Integer> resourceIdList) {
        resourceService.deleteResources(resourceIdList);
        return new Result<>(true, OK, DELETE);
    }

    @ApiOperation(value = "新增或修改资源")
    @PostMapping("/resources")
    public Result<?> saveOrUpdateResources(@RequestBody @Valid ResourceVO resourceVO) {
        resourceService.saveOrUpdateResource(resourceVO);
        return new Result<>(true, OK, OPERATE);
    }

    @ApiOperation(value = "查看角色资源选项")
    @GetMapping("/role/resources")
    public Result<List<LabelOptionDTO>> listResourceOption() {
        return new Result<>(true, OK, QUERY, resourceService.listResourceOption());
    }

}
