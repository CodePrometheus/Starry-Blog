package com.star.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.domain.entity.UserAuth;
import com.star.domain.vo.ConditionVO;
import com.star.service.dto.UserBackDTO;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 后台用户信息
 * @Author: zzStar
 * @Date: 12-18-2020 10:27
 */
@Repository
public interface UserAuthMapper extends BaseMapper<UserAuth> {

    /**
     * 查询后台用户列表
     *
     * @param condition 条件
     * @return 用户集合
     */
    List<UserBackDTO> listUsers(@Param("condition") ConditionVO condition);

    /**
     * 查询后台用户数量
     *
     * @param condition 条件
     * @return 用户数量
     */
    Integer countUser(@Param("condition") ConditionVO condition);

}