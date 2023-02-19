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
import java.lang.reflect.Method;
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
    private static Method method;

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
            String ipInfo = (String) method.invoke(searcher, ip);
            if (!StringUtils.isEmpty(ipInfo)) {
                ipInfo = ipInfo.replace("|0", "");
                ipInfo = ipInfo.replace("0|", "");
            }
            return ipInfo;
        } catch (Exception e) {
            log.error("getCityInfo exception:", e);
        }
        return "";
    }

    /**
     * 在服务启动时加载 ip2region.db 到内存中
     */
    @PostConstruct
    public static void initIp2regionResource() throws Exception {
        InputStream inputStream = new ClassPathResource("ip2region/ip2region.xdb").getInputStream();
        try {
            if (inputStream != null) {
                // 将 ip2region.db 转为 ByteArray
                byte[] dbBinStr = FileCopyUtils.copyToByteArray(inputStream);
                // 使用上述的 dbBinStr 创建一个完全基于内存的查询对象
                searcher = new Searcher(null, null, dbBinStr);
                // 二进制方式初始化 DBSearcher，需要使用基于内存的查找算法 memorySearch
                method = searcher.getClass().getMethod("search", String.class);
            }
        } finally {
            try {
                inputStream.close();
            } catch (Exception e) {
                log.error("inputStream.close() failed: {}", e);
            }
        }
    }

}
