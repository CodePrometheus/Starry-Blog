package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.domain.entity.Menu;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.service.dto.LabelOptionDTO;
import com.star.core.service.dto.MenuDTO;
import com.star.core.service.dto.UserMenuDTO;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 17:10
 */
public interface MenuService extends IService<Menu> {

    /**
     * 查看菜单列表
     * @param conditionVO 条件
     * @return 菜单列表
     */
    List<MenuDTO> listMenus(ConditionVO conditionVO);

    /**
     * 查看角色菜单选项
     * @return 角色菜单选项
     */
    List<LabelOptionDTO> listMenuOptions();

    /**
     * 查看用户菜单
     * @return 菜单列表
     */
    List<UserMenuDTO> listUserMenus();

}
