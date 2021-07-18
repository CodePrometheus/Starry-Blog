package com.star.core.handler;

import com.alibaba.fastjson.JSON;
import com.star.common.constant.Result;
import com.star.common.constant.StatusConst;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 用户权限处理
 *
 * @Author: zzStar
 * @Date: 12-18-2020 10:45
 */
@Component
public class AccessDeniedHandlerImpl implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AccessDeniedException e) throws IOException, ServletException {
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(new Result<>(false, StatusConst.ERROR, "没有操作权限")));
    }

}
