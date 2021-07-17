package com.star.common.exception;

import com.star.common.tool.IpUtil;
import com.star.common.tool.RedisUtil;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import static com.star.common.constant.RedisConst.*;

/**
 * Request监听
 *
 * @Author: zzStar
 * @Date: 12-21-2020 23:12
 */
@Component
public class ServletRequestListenerImpl implements ServletRequestListener {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        HttpSession session = request.getSession();
        String ip = (String) session.getAttribute(IP);
        //判断当前ip是否访问，增加访问量
        String ipAddr = IpUtil.getIpAddr(request);
        if (!ipAddr.equals(ip)) {
            session.setAttribute(IP, ipAddr);
            redisUtil.incr(BLOG_VIEWS_COUNT, 1);
        }
        //将ip存入redis，统计每日用户量
        redisUtil.set(IP_SET, ipAddr);
    }

    @Scheduled(cron = " 0 1 0 * * ?")
    private void clear() {
        //清空redis中的ip
        redisUtil.del(IP_SET);
    }


}
