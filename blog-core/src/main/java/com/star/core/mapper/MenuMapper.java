package com.star.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.core.entity.Menu;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 17:08
 */
@Repository
public interface MenuMapper extends BaseMapper<Menu> {

    /**
     * 根据用户id查询菜单
     *
     * @param userInfoId 用户信息id
     * @return 菜单列表
     */
    List<Menu> listMenuByUserInfoId(Integer userInfoId);

}
