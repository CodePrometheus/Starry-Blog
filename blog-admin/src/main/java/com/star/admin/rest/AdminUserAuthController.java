package com.star.admin.rest;

import com.star.admin.service.AdminUserAuthService;
import com.star.common.constant.Result;
import com.star.inf.dto.PageData;
import com.star.inf.dto.UserAreaDTO;
import com.star.inf.dto.UserBackDTO;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.PasswordVO;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@RestController
@RequestMapping("admin")
public class AdminUserAuthController {

    @Resource
    private AdminUserAuthService adminUserAuthService;

    @ApiOperation(value = "查看后台用户列表")
    @GetMapping("/admin/users")
    private Result<PageData<UserBackDTO>> listUsers(ConditionVO condition) {
        return Result.success(adminUserAuthService.listUserBack(condition));
    }

    @ApiOperation(value = "修改管理员密码")
    @PutMapping("/admin/users/password")
    private Result<?> updateAdminPassword(@Valid @RequestBody PasswordVO passwordVO) {
        adminUserAuthService.updateAdminPassword(passwordVO);
        return Result.success();
    }

    /**
     * 获取用户区域分布
     *
     * @param condition 条件
     * @return {@link Result<  UserAreaDTO  >} 用户区域分布
     */
    @ApiOperation(value = "获取用户区域分布")
    @GetMapping("/admin/user/area")
    public Result<List<UserAreaDTO>> listUserAreas(ConditionVO condition) {
        return Result.success(adminUserAuthService.listUserAreas(condition));
    }

}
