package com.star.rest;


import com.star.constant.Result;
import com.star.constant.StatusConst;
import com.star.domain.vo.ConditionVO;
import com.star.domain.vo.PasswordVO;
import com.star.domain.vo.UserVO;
import com.star.service.UserAuthService;
import com.star.service.dto.PageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private UserAuthService userAuthService;

    @ApiOperation(value = "发送邮箱验证码")
    @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String", dataTypeClass = String.class)
    @GetMapping("/users/code")
    private Result sendCode(String username) {
        userAuthService.sendCode(username);
        return new Result(true, StatusConst.OK, "发送成功！");
    }

    @ApiOperation(value = "查看后台用户列表")
    @GetMapping("/admin/users")
    private Result<PageDTO> listUsers(ConditionVO condition) {
        return new Result(true, StatusConst.OK, "查询成功！", userAuthService.listUserBackDTO(condition));
    }

    @ApiOperation(value = "用户注册")
    @PostMapping("/users")
    private Result saveUser(@Valid @RequestBody UserVO user) {
        userAuthService.saveUser(user);
        return new Result(true, StatusConst.OK, "注册成功！");
    }

    @ApiOperation(value = "修改密码")
    @PutMapping("/users/password")
    private Result updatePassword(@Valid @RequestBody UserVO user) {
        userAuthService.updatePassword(user);
        return new Result(true, StatusConst.OK, "修改成功！");
    }

    @ApiOperation(value = "修改管理员密码")
    @PutMapping("/admin/users/password")
    private Result updateAdminPassword(@Valid @RequestBody PasswordVO passwordVO) {
        userAuthService.updateAdminPassword(passwordVO);
        return new Result(true, StatusConst.OK, "修改成功！");
    }

}

