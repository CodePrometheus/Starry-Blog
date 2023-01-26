package com.star.admin.rest;

import com.star.admin.service.AdminRoleService;
import com.star.common.constant.Result;
import com.star.inf.dto.PageData;
import com.star.inf.dto.RoleDTO;
import com.star.inf.dto.UserRoleDTO;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.RoleDisableVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-16-2021 15:21
 */
@Api(tags = "角色模块")
@RestController
@RequestMapping("admin")
public class AdminRoleController {

    @Resource
    private AdminRoleService adminRoleService;

    @ApiOperation(value = "查询用户角色选项")
    @GetMapping("/users/role")
    public Result<List<UserRoleDTO>> listUserRole() {
        return Result.success(adminRoleService.listUserRoles());
    }

    @ApiOperation(value = "查询角色列表")
    @GetMapping("/roles")
    public Result<PageData<RoleDTO>> listRoles(ConditionVO condition) {
        return Result.success(adminRoleService.listRoles(condition));
    }

    @ApiOperation("删除角色")
    @DeleteMapping("/roles")
    public Result<?> deleteRoles(@RequestBody List<Integer> roleIdList) {
        adminRoleService.deleteRoles(roleIdList);
        return Result.success();
    }

    @ApiOperation("修改角色禁用状态")
    @PutMapping("/role/disable")
    public Result<?> updateRoleDisable(@Valid @RequestBody RoleDisableVO roleDisable) {
        adminRoleService.updateRoleDisable(roleDisable);
        return Result.success();
    }

}
