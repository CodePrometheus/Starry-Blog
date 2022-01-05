package com.star.core.hook;

import com.star.core.entity.Resource;
import com.star.core.entity.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 访问埋点器
 *
 * @Author: zzStar
 * @Date: 01-03-2022 22:17
 */
public interface LogPointer {

    /**
     * 埋点操作
     *
     * @param request  当前请求
     * @param response 当前响应
     * @param user     用户
     * @param resource 访问的资源
     * @param isAdmin  是否后台
     */
    void doPoint(HttpServletRequest request, HttpServletResponse response, UserInfo user, Resource resource, boolean isAdmin);

}
