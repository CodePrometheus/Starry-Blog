package com.star.inf.rest;

import com.star.common.constant.Result;
import com.star.inf.service.UserAuthServiceImpl;
import com.star.inf.vo.UserVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * 用户逻辑
 *
 * @Author: zzStar
 * @Date: 12-21-2020 15:58
 */
@RestController
@Api(tags = "用户账号模块")
public class UserAuthController {

    @Resource
    private UserAuthServiceImpl userAuthService;

    @ApiOperation(value = "发送邮箱验证码")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", dataTypeClass = String.class)
    @GetMapping("/users/code")
    private Result<?> sendCode(String username) {
        userAuthService.sendCode(username);
        return Result.success();
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("/register")
    private Result<?> register(@Valid @RequestBody UserVO user) {
        userAuthService.register(user);
        return Result.success();
    }

    @ApiOperation(value = "修改密码")
    @PutMapping("/users/password")
    private Result<?> updatePassword(@Valid @RequestBody UserVO user) {
        userAuthService.updatePassword(user);
        return Result.success();
    }

}

