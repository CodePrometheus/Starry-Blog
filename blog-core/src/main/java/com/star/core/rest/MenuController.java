package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.dto.LabelOptionDTO;
import com.star.core.dto.MenuDTO;
import com.star.core.dto.UserMenuDTO;
import com.star.core.service.MenuService;
import com.star.core.vo.ConditionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-16-2021 15:43
 */
@Api(tags = "菜单模块")
@RestController
@RequestMapping("admin")
public class MenuController {

    @Resource
    private MenuService menuService;

    @ApiOperation("查看菜单列表")
    @GetMapping("/menus")
    public Result<List<MenuDTO>> listMenus(ConditionVO conditionVO) {
        return Result.success(menuService.listMenus(conditionVO));
    }

    @ApiOperation("查看角色菜单选项")
    @GetMapping("/role/menus")
    public Result<List<LabelOptionDTO>> listMenuOptions() {
        return Result.success(menuService.listMenuOptions());
    }

    @ApiOperation(value = "查看用户菜单")
    @GetMapping("/user/menus")
    public Result<List<UserMenuDTO>> listUserMenus() {
        return Result.success(menuService.listUserMenus());
    }

}
