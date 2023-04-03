package com.star.core.handler;

import com.star.common.tool.IpUtils;
import com.star.common.tool.RedisUtils;
import jakarta.annotation.Resource;
import jakarta.servlet.ServletRequestEvent;
import jakarta.servlet.ServletRequestListener;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;

import static com.star.common.constant.RedisConst.*;

/**
 * Request 监听
 *
 * @Author: zzStar
 * @Date: 12-21-2020 23:12
 */
@Component
public class ServletRequestListenerHandler implements ServletRequestListener {

    @Resource
    private RedisUtils redisUtils;

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        HttpSession session = request.getSession();
        String ip = (String) session.getAttribute(IP);
        // 判断当前ip是否访问，增加访问量
        String ipAddr = IpUtils.getIpAddr(request);
        if (!ipAddr.equals(ip)) {
            session.setAttribute(IP, ipAddr);
            redisUtils.incr(BLOG_VIEWS_COUNT, 1);
        }
        // 将ip存入redis，统计每日用户量
        redisUtils.set(IP_SET, ipAddr);
    }

}
