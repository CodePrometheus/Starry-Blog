package com.star.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.core.domain.entity.RoleMenu;
import com.star.core.domain.mapper.RoleMenuMapper;
import com.star.core.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 17:13
 */
@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {
}
