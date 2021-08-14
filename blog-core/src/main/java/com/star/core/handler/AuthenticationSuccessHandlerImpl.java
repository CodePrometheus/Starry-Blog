package com.star.core.handler;

import com.alibaba.fastjson.JSON;
import com.star.common.constant.Result;
import com.star.core.dto.UserLoginDTO;
import com.star.core.entity.UserAuth;
import com.star.core.mapper.UserAuthMapper;
import com.star.core.util.BeanCopyUtil;
import com.star.core.util.UserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 登录成功处理
 *
 * @Author: zzStar
 * @Date: 12-18-2020 10:20
 */
@Component
public class AuthenticationSuccessHandlerImpl implements AuthenticationSuccessHandler {

    @Autowired(required = false)
    private UserAuthMapper userAuthMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        UserLoginDTO userLoginDTO = BeanCopyUtil.copyObject(UserUtil.getLoginUser(), UserLoginDTO.class);
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(Result.success(userLoginDTO)));
        // 更新用户ip，最近登录时间
        updateUserInfo();
    }


    public void updateUserInfo() {
        UserAuth userAuth = UserAuth.builder()
                .id(UserUtil.getLoginUser().getId())
                .ipAddr(UserUtil.getLoginUser().getIpAddr())
                .ipSource(UserUtil.getLoginUser().getIpSource())
                .lastLoginTime(UserUtil.getLoginUser().getLastLoginTime()).build();
        userAuthMapper.updateById(userAuth);
    }

}
