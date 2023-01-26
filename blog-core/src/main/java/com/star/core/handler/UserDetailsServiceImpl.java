package com.star.core.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.common.exception.StarryException;
import com.star.common.tool.IpUtils;
import com.star.common.tool.RedisUtils;
import com.star.inf.dto.UserInfoDTO;
import com.star.inf.entity.UserAuth;
import com.star.inf.entity.UserInfo;
import com.star.inf.mapper.RoleMapper;
import com.star.inf.mapper.UserAuthMapper;
import com.star.inf.mapper.UserInfoMapper;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.star.common.constant.RedisConst.*;

/**
 * @author zzStar
 */
@Service
@SuppressWarnings("unchecked")
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private HttpServletRequest request;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private UserAuthMapper userAuthMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isBlank(username)) {
            throw new StarryException("用户名不能为空！");
        }
        // 查询账号是否存在
        UserAuth user = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getId, UserAuth::getUserInfoId, UserAuth::getUsername,
                        UserAuth::getPassword, UserAuth::getLoginType)
                .eq(UserAuth::getUsername, username));
        if (Objects.isNull(user)) {
            throw new StarryException("用户名不存在");
        }

        // 封装信息
        return convertLoginUser(user, request);
    }

    /**
     * 封装用户登录信息
     *
     * @param user    用户账号
     * @param request 请求
     * @return 用户登录信息
     */
    public UserInfoDTO convertLoginUser(UserAuth user, HttpServletRequest request) {
        // 查询账号对应的信息
        UserInfo userInfo = userInfoMapper.selectById(user.getUserInfoId());
        List<String> roleList = roleMapper.listRolesByUserInfoId(userInfo.getId());
        Set<Object> articleLikeSet = redisUtils.sMembers(ARTICLE_USER_LIKE + userInfo.getId());
        Set<Object> commentLikeSet = redisUtils.sMembers(COMMENT_USER_LIKE + userInfo.getId());
        Set<Object> momentLikeSet = redisUtils.sMembers(MOMENT_USER_LIKE + userInfo.getId());

        // 获取登录信息
        String ipAddr = IpUtils.getIpAddr(request);
        String ipSource = IpUtils.getIpSource(ipAddr);

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
                .momentLikeSet(momentLikeSet)
                .ipAddr(ipAddr)
                .ipSource(ipSource)
                .isDisable(userInfo.getIsDisable())
                .lastLoginTime(LocalDateTime.now(ZoneId.of("Asia/Shanghai"))).build();
    }

}
