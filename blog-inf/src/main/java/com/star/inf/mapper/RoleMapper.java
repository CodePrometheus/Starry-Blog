package com.star.inf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.inf.dto.ResourceRoleDTO;
import com.star.inf.dto.RoleDTO;
import com.star.inf.entity.Role;
import com.star.inf.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 13:12
 */
@Repository
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户id获取角色列表
     *
     * @param userInfoId
     * @return
     */
    List<String> listRolesByUserInfoId(Integer userInfoId);

    /**
     * 查询角色列表
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return {@link List< RoleDTO >} 角色列表
     */
    List<RoleDTO> listRoles(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    /**
     * 查询路由角色列表
     *
     * @return 角色标签
     */
    List<ResourceRoleDTO> listUrlRoles();

}
