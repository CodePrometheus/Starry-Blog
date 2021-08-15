package com.star.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.core.dto.PageData;
import com.star.core.dto.RoleDTO;
import com.star.core.dto.UserRoleDTO;
import com.star.core.entity.Role;
import com.star.core.entity.RoleMenu;
import com.star.core.entity.RoleResource;
import com.star.core.entity.UserRole;
import com.star.core.handler.FilterInvocationSecurityMetadataSourceImpl;
import com.star.core.mapper.RoleMapper;
import com.star.core.mapper.UserRoleMapper;
import com.star.core.service.RoleMenuService;
import com.star.core.service.RoleResourceService;
import com.star.core.service.RoleService;
import com.star.core.util.BeanCopyUtil;
import com.star.core.util.PageUtils;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.RoleVO;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.FALSE;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 17:14
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMenuService roleMenuService;

    @Resource
    private FilterInvocationSecurityMetadataSourceImpl metadataSource;

    @Resource
    private RoleResourceService roleResourceService;

    @Resource
    private RoleMapper roleMapper;

    @Override
    public List<UserRoleDTO> listUserRoles() {
        // 获取用户角色选项
        List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getRoleName));
        return BeanCopyUtil.copyList(roleList, UserRoleDTO.class);
    }

    @Override
    public PageData<RoleDTO> listRoles(ConditionVO conditionVO) {
        // 查询角色列表
        List<RoleDTO> roleDTOList = roleMapper.listRoles(PageUtils.getLimitCurrent(), PageUtils.getSize(), conditionVO);
        Integer count = roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                .like(StringUtils.isNotBlank(conditionVO.getKeywords()),
                        Role::getRoleName, conditionVO.getKeywords()));
        return new PageData<>(roleDTOList, count);
    }

    @Override
    @Transactional(rollbackFor = SecurityException.class)
    public void saveOrUpdateRole(RoleVO roleVO) {
        // 是否已经存在
        Integer count = roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                .eq(Role::getRoleName, roleVO.getRoleName()));
        if (Objects.isNull(roleVO.getId()) && count > 0) {
            throw new StarryException("角色名已经存在");
        }
        // save Or update
        Role role = Role.builder()
                .id(roleVO.getId())
                .roleName(roleVO.getRoleName())
                .roleLabel(roleVO.getRoleLabel())
                .isDisable(FALSE)
                .createTime(Objects.isNull(roleVO.getId()) ? new Date() : null)
                .updateTime(Objects.nonNull(roleVO.getId()) ? new Date() : null).build();
        this.saveOrUpdateRole(roleVO);
        // 更新对应的RoleResource
        if (CollectionUtils.isNotEmpty(roleVO.getResourceIdList())) {
            if (Objects.nonNull(roleVO.getId())) {
                // 先全部删除
                roleResourceService.remove(new LambdaQueryWrapper<RoleResource>()
                        .eq(RoleResource::getRoleId, roleVO.getId()));
            }
            // Define
            List<RoleResource> roleResourceList = roleVO.getResourceIdList().stream()
                    .map(resourceId -> RoleResource.builder()
                            .roleId(role.getId())
                            .resourceId(resourceId).build())
                    .collect(Collectors.toList());
            roleResourceService.saveBatch(roleResourceList);
            // 重新加载角色资源信息
            metadataSource.clearDataSource();
        }

        // 更新菜单列表
        if (CollectionUtils.isNotEmpty(roleVO.getMenuIdList())) {
            if (Objects.nonNull(roleVO.getId())) {
                roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>()
                        .eq(RoleMenu::getRoleId, roleVO.getId()));
            }
            List<RoleMenu> roleMenuList = roleVO.getMenuIdList().stream()
                    .map(menuId -> RoleMenu.builder()
                            .roleId(role.getId())
                            .menuId(menuId).build())
                    .collect(Collectors.toList());
            roleMenuService.saveBatch(roleMenuList);
        }
    }

    @Override
    public void deleteRoles(List<Integer> roleIdList) {
        // 判断用户下是否有用户
        Integer count = userRoleMapper.selectCount(new LambdaQueryWrapper<UserRole>()
                .in(UserRole::getId, roleIdList));
        if (count > 0) {
            throw new StarryException("该角色下存在用户");
        }
        roleMapper.deleteBatchIds(roleIdList);
    }

}
