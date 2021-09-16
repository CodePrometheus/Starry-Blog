package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.dto.LabelOptionDTO;
import com.star.core.dto.MenuDTO;
import com.star.core.dto.UserMenuDTO;
import com.star.core.entity.Menu;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.MenuVO;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 17:10
 */
public interface MenuService extends IService<Menu> {

    /**
     * 查看菜单列表
     *
     * @param conditionVO 条件
     * @return 菜单列表
     */
    List<MenuDTO> listMenus(ConditionVO conditionVO);

    /**
     * 查看角色菜单选项
     *
     * @return 角色菜单选项
     */
    List<LabelOptionDTO> listMenuOptions();

    /**
     * 查看用户菜单
     *
     * @return 菜单列表
     */
    List<UserMenuDTO> listUserMenus();

    /**
     * 新增或修改菜单
     *
     * @param menuVO 菜单信息
     */
    void saveOrUpdateMenu(MenuVO menuVO);

    /**
     * 删除菜单
     *
     * @param menuId 菜单id
     */
    void deleteMenu(Integer menuId);

}
