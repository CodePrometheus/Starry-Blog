package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.dto.PageData;
import com.star.core.dto.UserOnlineDTO;
import com.star.core.service.UserInfoService;
import com.star.core.vo.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户信息逻辑
 *
 * @Author: zzStar
 * @Date: 12-20-2020 19:44
 */
@Api(tags = "用户信息模块")
@RestController
public class UserInfoController {

    @Resource
    private UserInfoService userInfoService;

    @ApiOperation(value = "修改用户资料")
    @PutMapping("/users/info")
    private Result<?> updateUserInfo(@Valid @RequestBody UserInfoVO userInfoVO) {
        userInfoService.updateUserInfo(userInfoVO);
        return Result.success();
    }

    @ApiOperation(value = "修改用户头像")
    @ApiImplicitParam(name = "file", value = "用户头像", required = true, dataType = "MultipartFile", dataTypeClass = MultipartFile.class)
    @PostMapping("/users/avatar")
    private Result<String> updateUserInfo(MultipartFile file) {
        return Result.success(userInfoService.updateUserAvatar(file));
    }

    @ApiOperation(value = "绑定用户邮箱")
    @PostMapping("/users/email")
    public Result<?> saveUserEmail(@Valid @RequestBody EmailVO emailVO) {
        userInfoService.saveUserEmail(emailVO);
        return Result.success();
    }

    @ApiOperation(value = "修改用户角色")
    @PutMapping("/admin/users/role")
    private Result<?> updateUserRole(@Valid @RequestBody UserRoleVO userRoleVO) {
        userInfoService.updateUserRole(userRoleVO);
        return Result.success();
    }

    @ApiOperation(value = "修改用户禁用状态")
    @PutMapping("/admin/users/disable/")
    private Result<String> updateUserSilence(@Valid @RequestBody UserDisableVO userDisableVO) {
        userInfoService.updateUserDisable(userDisableVO);
        return Result.success();
    }

    @ApiOperation("查看在线用户")
    @GetMapping("/admin/user/online")
    public Result<PageData<UserOnlineDTO>> listOnlineUsers(ConditionVO conditionVO) {
        return Result.success(userInfoService.listOnlineUsers(conditionVO));
    }

    @ApiOperation("移除在线用户")
    @DeleteMapping("/admin/user/online/{userInfoId}")
    public Result<PageData<UserOnlineDTO>> removeOnlineUser(@PathVariable("userInfoId") Integer userInfoId) {
        userInfoService.removeOnlineUser(userInfoId);
        return Result.success();
    }

}

