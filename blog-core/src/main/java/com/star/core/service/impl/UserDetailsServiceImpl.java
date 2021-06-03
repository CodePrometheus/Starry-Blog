package com.star.core.service.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.star.common.exception.StarryException;
import com.star.core.domain.entity.UserAuth;
import com.star.core.domain.entity.UserInfo;
import com.star.core.domain.mapper.UserAuthMapper;
import com.star.core.domain.mapper.UserInfoMapper;
import com.star.core.service.dto.UserInfoDTO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Set;

/**
 * @author zzStar
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Resource
    private UserAuthMapper userAuthMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private RedisTemplate redisTemplate;

    @Override
    public UserDetails loadUserByUsername(String username) {
        if (StringUtils.isEmpty(username)) {
            throw new StarryException("用户名不能为空！");
        }
        //查询账号是否存在
        QueryWrapper<UserAuth> userAuthQueryWrapper = new QueryWrapper<>();
        userAuthQueryWrapper.select("id", "user_info_id", "password").eq("username", username);
        UserAuth user = userAuthMapper.selectOne(userAuthQueryWrapper);
        if (user == null) {
            throw new StarryException("用户名不存在");
        }
        //查询账号对应的信息
        QueryWrapper<UserInfo> userInfoWrapper = new QueryWrapper<>();
        userInfoWrapper.select("id", "user_role", "nickname", "avatar", "intro", "web_site", "is_silence").eq("id", user.getUserInfoId());
        UserInfo userInfo = userInfoMapper.selectOne(userInfoWrapper);
        //查询账号点赞信息
        Set articleLikeSet = (Set<Integer>) redisTemplate.boundHashOps("article_user_like").get(userInfo.getId().toString());
        Set commentLikeSet = (Set) redisTemplate.boundHashOps("comment_user_like").get(userInfo.getId().toString());
        //封装信息
        return User.withUsername(JSON.toJSONString(new UserInfoDTO(user.getId(), userInfo, articleLikeSet, commentLikeSet))).password(user.getPassword()).roles(userInfo.getUserRole()).build();
    }

}
