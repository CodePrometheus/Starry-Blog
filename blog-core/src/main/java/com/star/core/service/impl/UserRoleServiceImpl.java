package com.star.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.core.domain.entity.UserRole;
import com.star.core.domain.mapper.UserRoleMapper;
import com.star.core.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 17:15
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {
}
