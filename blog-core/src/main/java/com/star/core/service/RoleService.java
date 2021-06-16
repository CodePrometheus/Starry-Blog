package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.domain.entity.Role;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.domain.vo.RoleVO;
import com.star.core.service.dto.PageDTO;
import com.star.core.service.dto.RoleDTO;
import com.star.core.service.dto.UserRoleDTO;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 17:10
 */
public interface RoleService extends IService<Role> {

    /**
     * 获取用户角色选项
     *
     * @return 角色
     */
    List<UserRoleDTO> listUserRoles();

    /**
     * 查询角色列表
     *
     * @param conditionVO 条件
     * @return 角色列表
     */
    PageDTO<RoleDTO> listRoles(ConditionVO conditionVO);

    /**
     * 保存或更新角色
     *
     * @param roleVO 角色
     */
    void saveOrUpdateRole(RoleVO roleVO);

    /**
     * 删除角色
     *
     * @param roleIdList 角色id列表
     */
    void deleteRoles(List<Integer> roleIdList);
}
