package com.star.core.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.core.domain.entity.Role;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.service.dto.RoleDTO;
import com.star.core.service.dto.UrlRoleDTO;
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
     * @param conditionVO 条件
     * @return 角色列表
     */
    List<RoleDTO> listRoles(@Param("conditionVO") ConditionVO conditionVO);

    /**
     * 查询路由角色列表
     *
     * @return 角色标签
     */
    List<UrlRoleDTO> listUrlRoles();

}
