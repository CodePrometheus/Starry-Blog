package com.star.core.util;

import com.alibaba.fastjson.JSON;
import com.star.core.service.dto.UserInfoDTO;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * 获取当前登录的用户
 *
 * @Author: zzStar
 * @Date: 12-16-2020 20:22
 */
public class UserUtil {
    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static UserInfoDTO getLoginUser() {
        return JSON.parseObject(SecurityContextHolder.getContext().getAuthentication().getName(), UserInfoDTO.class);
    }
}
