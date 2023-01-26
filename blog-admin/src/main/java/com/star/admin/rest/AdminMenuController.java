package com.star.admin.rest;

import com.star.admin.service.AdminMenuService;
import com.star.common.constant.Result;
import com.star.inf.dto.LabelOptionDTO;
import com.star.inf.dto.MenuDTO;
import com.star.inf.dto.UserMenuDTO;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.MenuVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-16-2021 15:43
 */
@Api(tags = "菜单模块")
@RestController
@RequestMapping("admin")
public class AdminMenuController {

    @Resource
    private AdminMenuService menuService;

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

    @ApiOperation(value = "新增或修改菜单")
    @PostMapping("/menus")
    public Result<?> saveOrUpdateMenu(@Valid @RequestBody MenuVO menuVO) {
        menuService.saveOrUpdateMenu(menuVO);
        return Result.success();
    }

    @ApiOperation(value = "删除菜单")
    @DeleteMapping("/menus/{menuId}")
    public Result<?> deleteMenu(@PathVariable("menuId") Integer menuId) {
        menuService.deleteMenu(menuId);
        return Result.success();
    }

}
