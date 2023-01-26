package com.star.core.config;

import com.alibaba.fastjson2.JSON;
import com.star.common.constant.Result;
import com.star.common.tool.BeanCopyUtil;
import com.star.inf.dto.UserLoginDTO;
import com.star.inf.entity.UserAuth;
import com.star.inf.mapper.UserAuthMapper;
import com.star.inf.utils.UserUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @Author: Starry
 * @Date: 01-24-2023
 */
@Component
public class AuthenticationFilterChainHandler implements AccessDeniedHandler, AuthenticationEntryPoint,
        AuthenticationFailureHandler, AuthenticationSuccessHandler, LogoutSuccessHandler {

    public static final String CONTENT_TYPE = "application/json;charset=utf-8";

    @Resource
    private UserAuthMapper userAuthMapper;

    /**
     * 用户未登录
     *
     * @param request       that resulted in an <code>AuthenticationException</code>
     * @param response      so that the user agent can begin authentication
     * @param authException that caused the invocation
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.setContentType(CONTENT_TYPE);
        response.getWriter().write(JSON.toJSONString(Result.fail("用户未登录")));
    }

    /**
     * 用户权限不足
     *
     * @param request               that resulted in an <code>AccessDeniedException</code>
     * @param response              so that the user agent can be advised of the failure
     * @param accessDeniedException that caused the invocation
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setContentType(CONTENT_TYPE);
        response.getWriter().write(JSON.toJSONString(Result.fail("权限不足")));
    }

    /**
     * 登录失败
     *
     * @param request  the request during which the authentication attempt occurred.
     * @param response the response.
     * @param e        the exception which was thrown to reject the authentication
     *                 request.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException e) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(Result.fail(e.getMessage())));
    }

    /**
     * 登录成功
     *
     * @param request        the request which caused the successful authentication
     * @param response       the response
     * @param authentication the <tt>Authentication</tt> object which was created during
     *                       the authentication process.
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        UserLoginDTO userLoginDTO = BeanCopyUtil.copyObject(UserUtils.getLoginUser(), UserLoginDTO.class);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(Result.success(userLoginDTO)));
        // 更新用户ip，最近登录时间
        updateUserInfo();
    }

    /**
     * 注销成功
     *
     * @param request
     * @param response
     * @param authentication
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(Result.success()));
    }

    @Async
    public void updateUserInfo() {
        UserAuth userAuth = UserAuth.builder()
                .id(UserUtils.getLoginUser().getId())
                .ipAddr(UserUtils.getLoginUser().getIpAddr())
                .ipSource(UserUtils.getLoginUser().getIpSource())
                .lastLoginTime(UserUtils.getLoginUser().getLastLoginTime()).build();
        userAuthMapper.updateById(userAuth);
    }

}
