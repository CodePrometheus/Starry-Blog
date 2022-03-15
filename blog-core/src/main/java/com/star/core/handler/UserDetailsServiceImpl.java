package com.star.core.handler;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.common.exception.StarryException;
import com.star.common.tool.IpUtil;
import com.star.common.tool.RedisUtil;
import com.star.core.dto.UserInfoDTO;
import com.star.core.entity.UserAuth;
import com.star.core.entity.UserInfo;
import com.star.core.mapper.RoleMapper;
import com.star.core.mapper.UserAuthMapper;
import com.star.core.mapper.UserInfoMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
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
    private RedisUtil redisUtil;

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
        Set<Object> articleLikeSet = redisUtil.sMembers(ARTICLE_USER_LIKE + userInfo.getId());
        Set<Object> commentLikeSet = redisUtil.sMembers(COMMENT_USER_LIKE + userInfo.getId());
        Set<Object> momentLikeSet = redisUtil.sMembers(MOMENT_USER_LIKE + userInfo.getId());

        // 获取登录信息
        String ipAddr = IpUtil.getIpAddr(request);
        String ipSource = IpUtil.getIpSource(ipAddr);

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
