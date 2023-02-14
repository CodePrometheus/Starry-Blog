package com.star.inf.rest;

import com.star.common.constant.Result;
import com.star.inf.service.UserInfoServiceImpl;
import com.star.inf.vo.EmailVO;
import com.star.inf.vo.UserInfoVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

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
    private UserInfoServiceImpl userInfoService;

    @ApiOperation(value = "修改用户资料")
    @PutMapping("/users/info")
    public Result<?> updateUserInfo(@Valid @RequestBody UserInfoVO userInfoVO) {
        userInfoService.updateUserInfo(userInfoVO);
        return Result.success();
    }

    @ApiOperation(value = "修改用户头像")
    @ApiImplicitParam(name = "file", value = "用户头像", required = true, dataType = "MultipartFile", dataTypeClass = MultipartFile.class)
    @PostMapping("/users/avatar")
    public Result<String> updateUserInfo(MultipartFile file) {
        return Result.success(userInfoService.updateUserAvatar(file));
    }

    @ApiOperation(value = "绑定用户邮箱")
    @PostMapping("/users/email")
    public Result<?> saveUserEmail(@Valid @RequestBody EmailVO emailVO) {
        userInfoService.saveUserEmail(emailVO);
        return Result.success();
    }

}

