package com.star.inf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.inf.entity.RoleMenu;
import com.star.inf.mapper.RoleMenuMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 17:13
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements IService<RoleMenu> {
}
