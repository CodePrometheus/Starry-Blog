package com.star.common.tool;

import jakarta.servlet.http.HttpServletRequest;
import nl.basjes.parse.useragent.UserAgent;
import nl.basjes.parse.useragent.UserAgentAnalyzer;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author: zzStar
 * @Date: 2022/1/7 9:57 AM
 */
@Component
public class UserAgentUtil {

    private final UserAgentAnalyzer uaa;

    public UserAgentUtil() {
        this.uaa = UserAgentAnalyzer
                .newBuilder()
                .hideMatcherLoadStats()
                .withField("OperatingSystemNameVersionMajor")
                .withField("AgentNameVersion")
                .build();
    }

    /**
     * 从User-Agent解析客户端操作系统和浏览器版本
     */
    public Map<String, String> parseOsAndBrowser(HttpServletRequest request) {
        UserAgent.ImmutableUserAgent agent = uaa.parse(request.getHeader("User-Agent"));
        String os = agent.getValue("OperatingSystemNameVersionMajor");
        String browser = agent.getValue("AgentNameVersion");
        Map<String, String> map = new HashMap<>();
        map.put("os", os);
        map.put("browser", browser);
        return map;
    }

}
