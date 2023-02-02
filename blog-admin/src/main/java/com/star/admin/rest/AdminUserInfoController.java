package com.star.admin.rest;

import com.star.admin.service.AdminUserInfoService;
import com.star.common.constant.Result;
import com.star.inf.dto.PageData;
import com.star.inf.dto.UserOnlineDTO;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.UserDisableVO;
import com.star.inf.vo.UserRoleVO;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@RestController
@RequestMapping("admin")
public class AdminUserInfoController {

    @Resource
    private AdminUserInfoService adminUserInfoService;

    @ApiOperation(value = "修改用户角色")
    @PutMapping("/users/role")
    private Result<?> updateUserRole(@Valid @RequestBody UserRoleVO userRoleVO) {
        adminUserInfoService.updateUserRole(userRoleVO);
        return Result.success();
    }

    @ApiOperation(value = "修改用户禁用状态")
    @PutMapping("/user/disable")
    private Result<?> updateUserDisable(@Valid @RequestBody UserDisableVO userDisableVO) {
        adminUserInfoService.updateUserDisable(userDisableVO);
        return Result.success();
    }

    @ApiOperation("查看在线用户")
    @GetMapping("/user/online")
    public Result<PageData<UserOnlineDTO>> listOnlineUsers(ConditionVO conditionVO) {
        return Result.success(adminUserInfoService.listOnlineUsers(conditionVO));
    }

    @ApiOperation("移除在线用户")
    @DeleteMapping("/user/online/{userInfoId}")
    public Result<PageData<UserOnlineDTO>> removeOnlineUser(@PathVariable("userInfoId") Integer userInfoId) {
        adminUserInfoService.removeOnlineUser(userInfoId);
        return Result.success();
    }

}
