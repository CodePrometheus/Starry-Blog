package com.star.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.core.entity.Menu;
import com.star.core.mapper.MenuMapper;
import com.star.core.vo.ConditionVO;
import com.star.core.service.MenuService;
import com.star.core.dto.LabelOptionDTO;
import com.star.core.dto.MenuDTO;
import com.star.core.dto.UserMenuDTO;
import com.star.core.util.BeanCopyUtil;
import com.star.core.util.UserUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.COMPONENT;
import static com.star.common.constant.CommonConst.TRUE;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 17:12
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Resource
    private MenuMapper menuMapper;

    @Override
    public List<MenuDTO> listMenus(ConditionVO conditionVO) {
        // 查询菜单数据
        List<Menu> menuList = this.list(new LambdaQueryWrapper<Menu>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()),
                        Menu::getName, conditionVO.getKeywords()));
        // 获取目录列表
        List<Menu> parentMenu = listParentMenu(menuList);
        // 获取parentMenu下的子菜单
        Map<Integer, List<Menu>> childrenMenu = listChildrenMenu(menuList);
        // 组装目录菜单数据
        return parentMenu.stream().map(item -> {
            MenuDTO menuDTO = BeanCopyUtil.copyObject(item, MenuDTO.class);
            // 获取目录下的菜单排序
            List<MenuDTO> menuDTOList = BeanCopyUtil.copyList(childrenMenu.get(item.getId()), MenuDTO.class).stream()
                    .sorted(Comparator.comparing(MenuDTO::getOrderNum))
                    .collect(Collectors.toList());
            menuDTO.setChildren(menuDTOList);
            return menuDTO;
        }).sorted(Comparator.comparing(MenuDTO::getOrderNum))
                .collect(Collectors.toList());
    }

    /**
     * 获取目录下菜单列表
     *
     * @param menuList 菜单列表
     * @return 目录下的菜单列表
     */
    private Map<Integer, List<Menu>> listChildrenMenu(List<Menu> menuList) {
        return menuList.stream()
                .filter(item -> Objects.nonNull(item.getParentId()))
                .collect(Collectors.groupingBy(Menu::getParentId));
    }

    /**
     * 获取目录列表
     *
     * @param menuList 菜单列表
     * @return 目录列表
     */
    private List<Menu> listParentMenu(List<Menu> menuList) {
        return menuList.stream()
                .filter(item -> Objects.isNull(item.getParentId()))
                .sorted(Comparator.comparing(Menu::getOrderNum))
                .collect(Collectors.toList());
    }

    @Override
    public List<LabelOptionDTO> listMenuOptions() {
        // 查询菜单数据
        List<Menu> menuList = this.list(new LambdaQueryWrapper<Menu>()
                .select(Menu::getId, Menu::getName, Menu::getParentId, Menu::getOrderNum));
        // 获取目录列表
        List<Menu> parentMenu = listParentMenu(menuList);
        // 获取parentMenu下的子菜单
        Map<Integer, List<Menu>> childrenMenu = listChildrenMenu(menuList);
        // 组装目录菜单数据
        return parentMenu.stream().map(item -> {
            // 获取目录下的菜单排序
            List<LabelOptionDTO> list = new ArrayList<>();
            List<Menu> children = childrenMenu.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                list = children.stream()
                        .sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> LabelOptionDTO.builder()
                                .id(menu.getId())
                                .labelName(menu.getName()).build())
                        .collect(Collectors.toList());
            }
            return LabelOptionDTO.builder()
                    .id(item.getId())
                    .labelName(item.getName())
                    .children(list).build();
        }).collect(Collectors.toList());
    }

    @Override
    public List<UserMenuDTO> listUserMenus() {
        // 查询用户菜单信息
        List<Menu> menuList = menuMapper.listMenuByUserInfoId(UserUtil.getUserInfoId());
        // 获取目录列表
        List<Menu> parentMenu = listParentMenu(menuList);
        // 获取目录下的子菜单
        Map<Integer, List<Menu>> childrenMenu = listChildrenMenu(menuList);
        // 转换前端菜单格式
        return convertUserMenuList(parentMenu, childrenMenu);
    }

    /**
     * 转换用户菜单格式
     *
     * @param parentMenu   目录
     * @param childrenMenu 子菜单
     */
    private List<UserMenuDTO> convertUserMenuList(List<Menu> parentMenu, Map<Integer, List<Menu>> childrenMenu) {
        return parentMenu.stream().map(item -> {
            // 获取目录
            UserMenuDTO userMenuDTO = new UserMenuDTO();
            List<UserMenuDTO> list = new ArrayList<>();

            // 获取目录下的子菜单
            List<Menu> children = childrenMenu.get(item.getId());
            if (CollectionUtils.isNotEmpty(children)) {
                // 多级菜单处理
                userMenuDTO = BeanCopyUtil.copyObject(item, UserMenuDTO.class);
                list = children.stream()
                        .sorted(Comparator.comparing(Menu::getOrderNum))
                        .map(menu -> {
                            UserMenuDTO menuDTO = BeanCopyUtil.copyObject(menu, UserMenuDTO.class);
                            menuDTO.setHidden(menu.getIsHidden().equals(TRUE));
                            return menuDTO;
                        }).collect(Collectors.toList());
            } else {
                // 一级菜单处理
                userMenuDTO.setPath(item.getPath());
                userMenuDTO.setComponent(COMPONENT);
                list.add(UserMenuDTO.builder()
                        .path("")
                        .name(item.getName())
                        .icon(item.getIcon())
                        .component(item.getComponent()).build());
            }
            userMenuDTO.setHidden(item.getIsHidden().equals(TRUE));
            userMenuDTO.setChildren(list);
            return userMenuDTO;
        }).collect(Collectors.toList());
    }

}
