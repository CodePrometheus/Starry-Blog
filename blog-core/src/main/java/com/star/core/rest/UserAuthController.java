package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.dto.PageData;
import com.star.core.dto.UserBackDTO;
import com.star.core.service.UserAuthService;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.PasswordVO;
import com.star.core.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * 用户逻辑
 *
 * @Author: zzStar
 * @Date: 12-21-2020 15:58
 */
@Api(tags = "用户账号模块")
@RestController
public class UserAuthController {

    @Resource
    private UserAuthService userAuthService;

    @ApiOperation(value = "发送邮箱验证码")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", dataTypeClass = String.class)
    @GetMapping("/users/code")
    private Result<?> sendCode(String username) {
        userAuthService.sendCode(username);
        return Result.success();
    }

    @ApiOperation(value = "查看后台用户列表")
    @GetMapping("/admin/users")
    private Result<PageData<UserBackDTO>> listUsers(ConditionVO condition) {
        return Result.success(userAuthService.listUserBackDTO(condition));
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("/users")
    private Result<?> saveUser(@Valid @RequestBody UserVO user) {
        userAuthService.saveUser(user);
        return Result.success();
    }

    @ApiOperation(value = "修改密码")
    @PutMapping("/users/password")
    private Result<?> updatePassword(@Valid @RequestBody UserVO user) {
        userAuthService.updatePassword(user);
        return Result.success();
    }

    @ApiOperation(value = "修改管理员密码")
    @PutMapping("/admin/users/password")
    private Result<?> updateAdminPassword(@Valid @RequestBody PasswordVO passwordVO) {
        userAuthService.updateAdminPassword(passwordVO);
        return Result.success();
    }

}

