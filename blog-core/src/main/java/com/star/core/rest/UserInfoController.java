package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.domain.vo.UserInfoVO;
import com.star.core.domain.vo.UserRoleVO;
import com.star.core.service.UserInfoService;
import com.star.core.service.dto.PageDTO;
import com.star.core.service.dto.UserOnlineDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;

import static com.star.common.constant.MessageConst.OPERATE;
import static com.star.common.constant.MessageConst.QUERY;
import static com.star.common.constant.StatusConst.OK;

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
        return new Result<>(true, OK, QUERY);
    }

    @ApiOperation(value = "修改用户头像")
    @ApiImplicitParam(name = "file", value = "用户头像", required = true, dataType = "MultipartFile", dataTypeClass = MultipartFile.class)
    @PostMapping("/users/avatar")
    private Result<String> updateUserInfo(MultipartFile file) {
        return new Result<>(true, OK, QUERY, userInfoService.updateUserAvatar(file));
    }

    @ApiOperation(value = "修改用户权限")
    @PutMapping("/admin/users/role")
    private Result<String> updateUserRole(@Valid @RequestBody UserRoleVO userRoleVO) {
        userInfoService.updateUserRole(userRoleVO);
        return new Result<>(true, OK, QUERY);
    }

    @ApiOperation(value = "修改用户禁用状态")
    @PutMapping("/admin/users/disable/{userInfoId}")
    private Result<String> updateUserSilence(@PathVariable("userInfoId") Integer userInfoId, Integer isDisable) {
        userInfoService.updateUserDisable(userInfoId, isDisable);
        return new Result<>(true, OK, QUERY);
    }

    @ApiOperation("查看在线用户")
    @GetMapping("/admin/user/online")
    public Result<PageDTO<UserOnlineDTO>> listOnlineUsers(ConditionVO conditionVO) {
        return new Result<>(true, OK, QUERY, userInfoService.listOnlineUsers(conditionVO));
    }

    @ApiOperation("移除在线用户")
    @DeleteMapping("/admin/user/online/{userInfoId}")
    public Result<PageDTO<UserOnlineDTO>> removeOnlineUser(@PathVariable("userInfoId") Integer userInfoId) {
        userInfoService.removeOnlineUser(userInfoId);
        return new Result<>(true, OK, OPERATE);
    }

}

