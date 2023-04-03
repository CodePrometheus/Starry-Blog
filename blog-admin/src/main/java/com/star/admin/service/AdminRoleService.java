package com.star.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.BeanCopyUtils;
import com.star.common.tool.PageUtils;
import com.star.inf.dto.PageData;
import com.star.inf.dto.RoleDTO;
import com.star.inf.dto.UserRoleDTO;
import com.star.inf.entity.Role;
import com.star.inf.entity.UserRole;
import com.star.inf.mapper.RoleMapper;
import com.star.inf.mapper.UserRoleMapper;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.RoleDisableVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@Service
public class AdminRoleService extends ServiceImpl<RoleMapper, Role> {

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private RoleMapper roleMapper;

    /**
     * 修改角色禁用状态
     *
     * @param roleDisable 角色禁用信息
     */
    @Transactional(rollbackFor = SecurityException.class)
    public void updateRoleDisable(RoleDisableVO roleDisable) {
        Role role = Role.builder().id(roleDisable.getId())
                .isDisable(roleDisable.getIsDisable()).build();
        roleMapper.updateById(role);
    }

    /**
     * 获取用户角色选项
     *
     * @return 角色
     */
    public List<UserRoleDTO> listUserRoles() {
        // 获取用户角色选项
        List<Role> roleList = roleMapper.selectList(new LambdaQueryWrapper<Role>()
                .select(Role::getId, Role::getRoleName));
        return BeanCopyUtils.copyList(roleList, UserRoleDTO.class);
    }


    /**
     * 查询角色列表
     *
     * @param condition 条件
     * @return 角色列表
     */
    public PageData<RoleDTO> listRoles(ConditionVO condition) {
        // 查询角色列表
        List<RoleDTO> roleList = roleMapper.listRoles(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        Long count = roleMapper.selectCount(new LambdaQueryWrapper<Role>()
                .like(StringUtils.isNotBlank(condition.getKeywords()),
                        Role::getRoleName, condition.getKeywords()));
        return new PageData<>(roleList, count);
    }

    /**
     * 删除角色
     *
     * @param roleIdList 角色id列表
     */
    @Transactional(rollbackFor = SecurityException.class)
    public void deleteRoles(List<Integer> roleIdList) {
        // 判断用户下是否有用户
        Long count = userRoleMapper.selectCount(new LambdaQueryWrapper<UserRole>()
                .in(UserRole::getId, roleIdList));
        if (count > 0) {
            throw new StarryException("该角色下存在用户");
        }
        roleMapper.deleteBatchIds(roleIdList);
    }

}
