package com.star.core.config.handler;


import com.alibaba.fastjson.JSON;
import com.star.common.constant.Result;
import com.star.common.constant.StatusConst;
import com.star.common.tool.IpUtil;
import com.star.core.domain.entity.UserAuth;
import com.star.core.domain.mapper.UserAuthMapper;
import com.star.core.service.dto.UserInfoDTO;
import com.star.core.util.UserUtil;
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

    @Resource
    private UserAuthMapper userAuthMapper;

    @Resource
    private HttpServletRequest request;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException {
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(new Result<UserInfoDTO>(true, StatusConst.OK, "登录成功！", UserUtil.getLoginUser())));
        // 更新用户登录ip地址，最新登录时间
        String ipAddr = IpUtil.getIpAddr(request);
        String ipSource = IpUtil.getIpSource(ipAddr);
        userAuthMapper.updateById(new UserAuth(ipAddr, ipSource));
    }

}
