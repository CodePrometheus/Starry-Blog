package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.dto.PageData;
import com.star.core.dto.RoleDTO;
import com.star.core.dto.UserRoleDTO;
import com.star.core.service.RoleService;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.RoleVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-16-2021 15:21
 */
@Api(tags = "角色模块")
@RestController("admin")
public class RoleController {

    @Resource
    private RoleService roleService;

    @ApiOperation(value = "查询用户角色选项")
    @GetMapping("/users/role")
    public Result<List<UserRoleDTO>> listUserRole() {
        return Result.success(roleService.listUserRoles());
    }

    @ApiOperation(value = "查询角色列表")
    @GetMapping("/roles")
    public Result<PageData<RoleDTO>> listRoles(ConditionVO conditionVO) {
        return Result.success(roleService.listRoles(conditionVO));
    }

    @ApiOperation(value = "保存或更新角色")
    @PostMapping("/role")
    public Result<?> listRoles(@RequestBody @Valid RoleVO roleVO) {
        roleService.saveOrUpdateRole(roleVO);
        return Result.success();
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/roles")
    public Result<?> deleteRoles(@RequestBody List<Integer> roleIdList) {
        roleService.deleteRoles(roleIdList);
        return Result.success();
    }

}
