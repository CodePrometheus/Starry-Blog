package com.star.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.core.dto.UserBackDTO;
import com.star.core.entity.UserAuth;
import com.star.core.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
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
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return {@link List<UserBackDTO>} 用户列表
     */
    List<UserBackDTO> listUsers(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

    /**
     * 查询后台用户数量
     *
     * @param condition 条件
     * @return 用户数量
     */
    Integer countUser(@Param("condition") ConditionVO condition);

}
