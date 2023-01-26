package com.star.inf.utils;

import com.star.inf.dto.UserInfoDTO;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * 获取当前登录的用户
 *
 * @Author: zzStar
 * @Date: 12-16-2020 20:22
 */
@Component
public class UserUtils {

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static UserInfoDTO getLoginUser() {
        return (UserInfoDTO) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static Integer getUserInfoId() {
        return getLoginUser().getUserInfoId();
    }

}
