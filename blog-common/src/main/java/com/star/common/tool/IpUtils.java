package com.star.common.tool;

import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.lionsoul.ip2region.xdb.Searcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * ip2region总体分为两个大版本：
 * <p>
 * 1.x：提供三种算法，其中内存查询速度在0.1x毫秒级别，离线库文件ip2region.db
 * <p>
 * 2.x：同样提供三种算法，其中内存查询速度在微秒级别，离线库文件ip2region.xdb
 * newWithFileOnly
 * newWithVectorIndex
 * newWithBuffer
 *
 * @Description: 用户工具类
 * @Author: zzStar
 * @Date: 12-23-2020 17:25
 */
@Component
@SuppressWarnings("all")
public class IpUtils {

    private static final Logger log = LoggerFactory.getLogger(IpUtils.class);

    private static Searcher searcher;

    /**
     * 在 Nginx 等代理之后获取用户真实 IP 地址
     * request -> IP
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("X-Real-IP");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("x-forwarded-for");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            if ("127.0.0.1".equals(ip) || "0:0:0:0:0:0:0:1".equals(ip)) {
                // 根据网卡取本机配置的IP
                InetAddress inet = null;
                try {
                    inet = InetAddress.getLocalHost();
                } catch (UnknownHostException e) {
                    log.error("getIpAddr exception:", e);
                }
                ip = inet.getHostAddress();
            }
        }
        return StringUtils.substringBefore(ip, ",");
    }

    /**
     * 根据 ip 从 ip2region.db 中获取地理位置
     * Ip -> CityInfo
     *
     * @param ip
     * @return
     */
    public static String getIpSource(String ip) {
        try {
            String res = searcher.search(ip);
            if (!StringUtils.isEmpty(res)) {
                res = res.replace("|0", "");
                res = res.replace("0|", "");
            }
            return res;
        } catch (Exception e) {
            log.error("getIpSource exception: ", e);
        }
        return null;
    }

    /**
     * 在服务启动时加载 ip2region.db 到内存中
     */
    @PostConstruct
    public static void initIp2regionResource() {
        try {
            InputStream inputStream = new ClassPathResource("ip2region/ip2region.xdb").getInputStream();
            if (inputStream != null) {
                byte[] dbBinStr;
                try {
                    // 将 ip2region.xdb 转为 ByteArray
                    dbBinStr = FileCopyUtils.copyToByteArray(inputStream);
                    // 并发使用，用整个 xdb 数据缓存创建的查询对象可以安全的用于并发，也即可以把这个 searcher 对象做成全局对象去跨线程访问
                    searcher = Searcher.newWithBuffer(dbBinStr);
                } catch (Exception e) {
                    log.error("initIp2regionResource Searcher failed: {}", e);
                } finally {
                    try {
                        inputStream.close();
                    } catch (Exception e) {
                        log.error("inputStream.close() failed: {}", e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("initIp2regionResource exception:", e);
        }
    }

}
