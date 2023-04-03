package com.star.core.blog;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.star.admin.service.AdminResourceService;
import com.star.admin.service.AdminRoleService;
import com.star.common.exception.StarryException;
import com.star.common.tool.BeanCopyUtils;
import com.star.core.config.FilterInvocationSecurityMetadataSourceImpl;
import com.star.core.hook.JwtHooks;
import com.star.inf.entity.BlogResource;
import com.star.inf.entity.Role;
import com.star.inf.entity.RoleMenu;
import com.star.inf.entity.RoleResource;
import com.star.inf.mapper.RoleMapper;
import com.star.inf.service.RoleMenuServiceImpl;
import com.star.inf.service.RoleResourceServiceImpl;
import com.star.inf.utils.UserUtils;
import com.star.inf.vo.ResourceVO;
import com.star.inf.vo.RoleVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.FALSE;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@Service
public class CommonService {

    @Resource
    private FilterInvocationSecurityMetadataSourceImpl metadataSource;

    @Resource
    private AdminResourceService adminResourceService;

    @Resource
    private AdminRoleService adminRoleService;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleResourceServiceImpl roleResourceServiceImpl;

    @Resource
    private RoleMenuServiceImpl roleMenuServiceImpl;

    @Resource
    private JwtHooks jwtHooks;

    /**
     * 添加或修改资源
     *
     * @param resourceVO 资源对象
     */
    @Transactional(rollbackFor = SecurityException.class)
    public void saveOrUpdateResource(ResourceVO resourceVO) {
        // 更新资源信息
        BlogResource blogResource = BeanCopyUtils.copyObject(resourceVO, BlogResource.class);
        adminResourceService.saveOrUpdate(blogResource);
        // 重新加载角色资源信息
        metadataSource.clearDataSource();
    }

    /**
     * 保存或修改用户角色
     *
     * @param roleVO
     */
    @Transactional(rollbackFor = SecurityException.class)
    public void saveOrUpdateRole(RoleVO roleVO) {
        // 是否已经存在
        Role existRole = roleMapper.selectOne(new LambdaQueryWrapper<Role>()
                .select(Role::getId)
                .eq(Role::getRoleName, roleVO.getRoleName()));
        if (Objects.nonNull(existRole) && !existRole.getId().equals(roleVO.getId())) {
            throw new StarryException("角色名已存在");
        }
        // save Or update
        Role role = Role.builder()
                .id(roleVO.getId())
                .roleName(roleVO.getRoleName())
                .roleLabel(roleVO.getRoleLabel())
                .isDisable(FALSE).build();
        adminRoleService.saveOrUpdate(role);
        // 更新对应的RoleResource
        if (CollectionUtils.isNotEmpty(roleVO.getResourceIdList())) {
            if (Objects.nonNull(roleVO.getId())) {
                // 先全部删除
                roleResourceServiceImpl.remove(new LambdaQueryWrapper<RoleResource>()
                        .eq(RoleResource::getRoleId, roleVO.getId()));
            }
            // Define
            List<RoleResource> roleResourceList = roleVO.getResourceIdList().stream()
                    .map(resourceId -> RoleResource.builder()
                            .roleId(role.getId())
                            .resourceId(resourceId).build())
                    .collect(Collectors.toList());
            roleResourceServiceImpl.saveBatch(roleResourceList);
            // 重新加载角色资源信息
            metadataSource.clearDataSource();
        }

        // 更新菜单列表
        if (CollectionUtils.isNotEmpty(roleVO.getMenuIdList())) {
            if (Objects.nonNull(roleVO.getId())) {
                roleMenuServiceImpl.remove(new LambdaQueryWrapper<RoleMenu>()
                        .eq(RoleMenu::getRoleId, roleVO.getId()));
            }
            List<RoleMenu> roleMenuList = roleVO.getMenuIdList().stream()
                    .map(menuId -> RoleMenu.builder()
                            .roleId(role.getId())
                            .menuId(menuId).build())
                    .collect(Collectors.toList());
            roleMenuServiceImpl.saveBatch(roleMenuList);
        }
    }

    /**
     * 用户登出
     *
     * @return
     */
    public void logout() {
        jwtHooks.delLoginUser(UserUtils.getUserInfoId());
    }

}
