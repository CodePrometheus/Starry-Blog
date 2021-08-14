package com.star.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.common.exception.StarryException;
import com.star.common.tool.RedisUtil;
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
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.star.common.constant.RedisConst.ARTICLE_USER_LIKE;
import static com.star.common.constant.RedisConst.COMMENT_USER_LIKE;
import static com.star.core.util.UserUtil.convertLoginUser;


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

        // 查询账号对应的信息
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getId, UserInfo::getEmail, UserInfo::getNickname, UserInfo::getAvatar,
                        UserInfo::getIntro, UserInfo::getWebSite, UserInfo::getIsDisable)
                .eq(UserInfo::getId, user.getUserInfoId()));

        // 查询账号对应的角色信息
        List<String> roleList = roleMapper.listRolesByUserInfoId(userInfo.getId());
        // 查询账号点赞信息
        Set<Integer> articleLikeSet = (Set) redisUtil.hGet(ARTICLE_USER_LIKE, userInfo.getId().toString());
        Set<Integer> commentLikeSet = (Set) redisUtil.hGet(COMMENT_USER_LIKE, userInfo.getId().toString());
        // 封装信息
        return convertLoginUser(user, userInfo, roleList, articleLikeSet, commentLikeSet, request);
    }

}
