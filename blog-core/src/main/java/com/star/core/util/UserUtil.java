package com.star.core.util;

import com.star.common.tool.IpUtil;
import com.star.core.entity.UserAuth;
import com.star.core.entity.UserInfo;
import com.star.core.dto.UserInfoDTO;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 获取当前登录的用户
 *
 * @Author: zzStar
 * @Date: 12-16-2020 20:22
 */
@Component
public class UserUtil {

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
