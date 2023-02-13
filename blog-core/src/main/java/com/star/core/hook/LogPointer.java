package com.star.core.hook;

import com.star.inf.entity.BlogResource;
import com.star.inf.entity.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
     * @param request      当前请求
     * @param response     当前响应
     * @param user         用户
     * @param blogResource 访问的资源
     * @param isAdmin      是否后台
     */
    void doPoint(HttpServletRequest request, HttpServletResponse response,
                 UserInfo user, BlogResource blogResource, boolean isAdmin);

}
