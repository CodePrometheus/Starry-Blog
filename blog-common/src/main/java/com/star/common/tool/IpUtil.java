package com.star.common.tool;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.star.common.constant.StarryConst;
import lombok.extern.slf4j.Slf4j;
import org.lionsoul.ip2region.DataBlock;
import org.lionsoul.ip2region.DbConfig;
import org.lionsoul.ip2region.DbSearcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ClassPathResource;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Description: 用户工具类
 * @Author: zzStar
 * @Date: 12-23-2020 17:25
 */
@Slf4j
@SuppressWarnings("all")
public class IpUtil {

    private static boolean ipLocal = false;
    private static File file = null;
    private static DbConfig config;
    private static final char SEPARATOR = '_';
    private static final String UNKNOWN = "unknown";
    public static final String SYS_TEM_DIR = System.getProperty("java.io.tmpdir") + File.separator;
    private static final List<CallBack> CALL_BACKS = new ArrayList<>();
    private static ApplicationContext applicationContext = null;
    private static final Logger log = LoggerFactory.getLogger(IpUtil.class);


    static {
        addCallBacks(() -> {
            IpUtil.ipLocal = getProperties("ip.local-parsing", false, Boolean.class);
            if (ipLocal) {
                /*
                 * 此文件为独享 ，不必关闭
                 */
                String path = "ip2region/ip2region.db";
                String name = "ip2region.db";
                try {
                    config = new DbConfig();
                    file = inputStreamToFile(new ClassPathResource(path).getInputStream(), name);
                } catch (Exception e) {
                    log.error(e.getMessage(), e);
                }
            }
        });
    }

    /**
     * 在Nginx等代理之后获取用户真实IP地址
     *
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || UNKNOWN.equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        String comma = ",";
        String localhost = "127.0.0.1";
        if (ip.contains(comma)) {
            ip = ip.split(",")[0];
        }
        if (localhost.equals(ip)) {
            // 获取本机真正的ip地址
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                log.error(e.getMessage(), e);
            }
        }
        return ip;
    }

    /**
     * 解析ip地址获取具体的位置
     *
     * @param ipAddress
     * @return
     */
    public static String getIpSource(String ip) {
        if (ipLocal) {
            return getLocalCityInfo(ip);
        } else {
            return getHttpCityInfo(ip);
        }
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getLocalCityInfo(String ip) {
        try {
            DataBlock dataBlock = new DbSearcher(config, file.getPath())
                    .binarySearch(ip);
            String region = dataBlock.getRegion();
            String address = region.replace("0|", "");
            char symbol = '|';
            if (address.charAt(address.length() - 1) == symbol) {
                address = address.substring(0, address.length() - 1);
            }
            return address.equals(StarryConst.REGION) ? "内网IP" : address;
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * 根据ip获取详细地址
     */
    public static String getHttpCityInfo(String ip) {
        String api = String.format(StarryConst.Url.IP_URL, ip);
        JSONObject object = JSONUtil.parseObj(HttpUtil.get(api));
        return object.get("addr", String.class);
    }

    private static DbSearcher searcher;
    private static Method method;


    /**
     * inputStream 转 File
     */
    static File inputStreamToFile(InputStream ins, String name) {
        File file = new File(SYS_TEM_DIR + name);
        if (file.exists()) {
            return file;
        }
        OutputStream os = null;
        try {
            os = new FileOutputStream(file);
            int bytesRead;
            int len = 8192;
            byte[] buffer = new byte[len];
            while ((bytesRead = ins.read(buffer, 0, len)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(os);
            close(ins);
        }
        return file;
    }

    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // 静默关闭
            }
        }
    }

    /**
     * 针对 某些初始化方法，在SpringContextHolder 未初始化时 提交回调方法。
     * 在SpringContextHolder 初始化后，进行回调使用
     *
     * @param callBack 回调函数
     */
    public synchronized static void addCallBacks(CallBack callBack) {
        if (true) {
            CALL_BACKS.add(callBack);
        } else {
            log.warn("CallBack：{} 已无法添加！立即执行", callBack.getCallBackName());
            callBack.executor();
        }
    }

    /**
     * 获取SpringBoot 配置信息
     *
     * @param property     属性key
     * @param defaultValue 默认值
     * @param requiredType 返回类型
     * @return /
     */
    public static <T> T getProperties(String property, T defaultValue, Class<T> requiredType) {
        T result = defaultValue;
        try {
            result = getBean(Environment.class).getProperty(property, requiredType);
        } catch (Exception ignored) {
        }
        return result;
    }

    /**
     * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
     */
    public static <T> T getBean(Class<T> requiredType) {
        assertContextInjected();
        return applicationContext.getBean(requiredType);
    }

    /**
     * 检查ApplicationContext不为空.
     */
    private static void assertContextInjected() {
        if (applicationContext == null) {
            throw new IllegalStateException("applicaitonContext属性未注入, 请在applicationContext" +
                    ".xml中定义SpringContextHolder或在SpringBoot启动类中注册SpringContextHolder.");
        }
    }


}
