package com.star.inf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.inf.entity.UserRole;
import com.star.inf.mapper.UserRoleMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 17:15
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements IService<UserRole> {
}
