package com.star.common.exception;

import com.star.common.tool.IpUtil;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


/**
 * Request监听
 *
 * @Author: zzStar
 * @Date: 12-21-2020 23:12
 */
@Component
public class ServletRequestListenerImpl implements ServletRequestListener {

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        HttpSession session = request.getSession();
        String ip = (String) session.getAttribute("ip");
        //判断当前ip是否访问，增加访问量
        String ipAddr = IpUtil.getIpAddr(request);
        if (!ipAddr.equals(ip)) {
            session.setAttribute("ip", ipAddr);
            redisTemplate.boundValueOps("blog_views_count").increment(1);
        }
        //将ip存入redis，统计每日用户量
        redisTemplate.boundSetOps("ip_set").add(ipAddr);
    }

    @Scheduled(cron = " 0 1 0 * * ?")
    private void clear() {
        //清空redis中的ip
        redisTemplate.delete("ip_set");
    }


}
