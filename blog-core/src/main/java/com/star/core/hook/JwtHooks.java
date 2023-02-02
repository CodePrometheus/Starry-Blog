package com.star.core.hook;

import com.alibaba.fastjson2.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.star.common.tool.RedisUtils;
import com.star.inf.dto.UserDetailsDTO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.star.common.constant.RedisConst.LOGIN_USER;

/**
 * @Author: Starry
 * @Date: 01-28-2023
 */
@Component
public class JwtHooks {

    private static final Logger log = LoggerFactory.getLogger(JwtHooks.class);

    @Value("${jwt.hold-time}")
    private int holeTime;

    @Value("${jwt.expire-time}")
    private int expireTime;

    @Value("${jwt.header}")
    private String header;

    @Value("${jwt.prefix}")
    private String prefix;

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.issuer}")
    private String issuer;

    @Resource
    private RedisUtils redisUtils;

    /**
     * userDetailsDTO => token
     *
     * @param userDetailsDTO
     * @return
     */
    public String createToken(UserDetailsDTO userDetailsDTO) {
        refreshToken(userDetailsDTO);
        Algorithm sign = Algorithm.HMAC256(secret);
        return JWT.create()
                .withJWTId(getUuid())
                .withSubject(String.valueOf(userDetailsDTO.getId()))
                .withIssuer(issuer)
                .sign(sign);
    }

    /**
     * refresh Token
     *
     * @param userDetailsDTO
     */
    public void refreshToken(UserDetailsDTO userDetailsDTO) {
        // 失效时间延迟
        userDetailsDTO.setExpireTime(LocalDateTime.now().plusSeconds(expireTime));
        // login_user > user_id | user_info
        redisUtils.hSet(LOGIN_USER, String.valueOf(userDetailsDTO.getId()), userDetailsDTO, expireTime);
    }

    /**
     * 未失效则延长时间
     *
     * @param userDetailsDTO
     */
    public void renewToken(UserDetailsDTO userDetailsDTO) {
        if (Duration.between(LocalDateTime.now(),
                userDetailsDTO.getExpireTime()).toMinutes() <= holeTime) {
            refreshToken(userDetailsDTO);
        }
    }

    /**
     * 解析 token
     *
     * @param token
     * @return user_id
     */
    public String parseToken(String token) {
        Algorithm algorithm = Algorithm.HMAC256(secret);
        return JWT.require(algorithm).build().verify(token).getClaim("sub").asString();
    }

    /**
     * 获取用户信息
     *
     * @param request
     * @return
     */
    public UserDetailsDTO getUserDetails(HttpServletRequest request) {
        String token = Optional.ofNullable(request.getHeader(header))
                .orElse("").replaceFirst(prefix, "");
        if (StringUtils.hasText(token) && !token.equals("null")) {
            String userId = parseToken(token);
            return JSONObject.parseObject(String.valueOf(redisUtils.hGet(LOGIN_USER, userId)), UserDetailsDTO.class);
        }
        return null;
    }

    /**
     * logout
     *
     * @param userId
     */
    public void delLoginUser(Integer userId) {
        redisUtils.hDel(LOGIN_USER, String.valueOf(userId));
    }

    public String getUuid() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

}
