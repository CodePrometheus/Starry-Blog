package com.star.core.util;

import com.alibaba.fastjson.JSON;
import com.star.common.tool.IpUtil;
import com.star.core.domain.entity.UserAuth;
import com.star.core.domain.entity.UserInfo;
import com.star.core.service.dto.UserInfoDTO;
import eu.bitwalker.useragentutils.UserAgent;
import org.springframework.security.core.context.SecurityContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * 获取当前登录的用户
 *
 * @Author: zzStar
 * @Date: 12-16-2020 20:22
 */
public class UserUtil {

    /**
     * 获取当前登录用户
     *
     * @return
     */
    public static UserInfoDTO getLoginUser() {
        return JSON.parseObject(SecurityContextHolder.getContext().getAuthentication().getName(), UserInfoDTO.class);
    }

    /**
     * 封装用户登录信息
     *
     * @param user           用户账号
     * @param userInfo       用户信息
     * @param articleLikeSet 点赞文章id集合
     * @param commentLikeSet 点赞评论id集合
     * @param request        请求
     * @return 用户登录信息
     */
    public static UserInfoDTO convertLoginUser(UserAuth user, UserInfo userInfo, List<String> roleList, Set<Integer> articleLikeSet, Set<Integer> commentLikeSet, HttpServletRequest request) {
        // 获取登录信息
        String ipAddr = IpUtil.getIpAddr(request);
        String ipSource = IpUtil.getIpSource(ipAddr);
        UserAgent userAgent = UserAgent.parseUserAgentString(request.getHeader("User-Agent"));

        // 封装权限集合
        return UserInfoDTO.builder()
                .id(user.getId())
                .userInfoId(userInfo.getId())
                .email(userInfo.getEmail())
                .loginType(user.getLoginType())
                .username(user.getUsername())
                .password(user.getPassword())
                .roleList(roleList)
                .nickname(userInfo.getNickname())
                .avatar(userInfo.getAvatar())
                .intro(userInfo.getIntro())
                .webSite(userInfo.getWebSite())
                .articleLikeSet(articleLikeSet)
                .commentLikeSet(commentLikeSet)
                .ipAddr(ipAddr)
                .ipSource(ipSource)
                .browser(userAgent.getBrowser().getName())
                .os(userAgent.getOperatingSystem().getName())
                .lastLoginTime(new Date()).build();
    }

}
