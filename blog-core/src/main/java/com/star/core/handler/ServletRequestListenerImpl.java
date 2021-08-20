package com.star.core.handler;

import com.star.common.tool.IpUtil;
import com.star.common.tool.RedisUtil;
import eu.bitwalker.useragentutils.Browser;
import eu.bitwalker.useragentutils.OperatingSystem;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

import static com.star.common.constant.RedisConst.BLOG_VIEWS_COUNT;
import static com.star.common.constant.RedisConst.UNIQUE_VISITOR;

/**
 * @Author: zzStar
 * @Date: 2021/8/20
 * @Description:
 */
@Component("ServletRequestListenerImpl")
public class ServletRequestListenerImpl implements ServletRequestListener {

    @Resource
    private RedisUtil redisUtil;

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        String ipAddr = IpUtil.getIpAddr(request);
        UserAgent userAgent = IpUtil.getUserAgent(request);
        Browser browser = userAgent.getBrowser();
        OperatingSystem operatingSystem = userAgent.getOperatingSystem();
        // 生成唯一用户标识
        String uuid = ipAddr + browser.getName() + operatingSystem.getName();
        String md5 = DigestUtils.md5DigestAsHex(uuid.getBytes());
        // 判断是否访问
        if (!redisUtil.sIsMember(UNIQUE_VISITOR, md5)) {
            redisUtil.incr(BLOG_VIEWS_COUNT, 1);
        }
        redisUtil.sAdd(UNIQUE_VISITOR, md5);
    }

}
