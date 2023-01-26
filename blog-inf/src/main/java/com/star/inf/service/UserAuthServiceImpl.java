package com.star.inf.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.constant.RabbitmqConst;
import com.star.common.exception.StarryException;
import com.star.common.tool.RedisUtils;
import com.star.inf.dto.EmailDTO;
import com.star.inf.entity.UserAuth;
import com.star.inf.entity.UserInfo;
import com.star.inf.entity.UserRole;
import com.star.inf.mapper.UserAuthMapper;
import com.star.inf.mapper.UserInfoMapper;
import com.star.inf.mapper.UserRoleMapper;
import com.star.inf.vo.UserVO;
import jakarta.annotation.Resource;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.star.common.constant.CommonConst.DEFAULT_AVATAR;
import static com.star.common.constant.CommonConst.DEFAULT_NICKNAME;
import static com.star.common.constant.LoginTypeConst.EMAIL;
import static com.star.common.constant.RedisConst.CODE_EXPIRE_TIME;
import static com.star.common.constant.RedisConst.CODE_KEY;
import static com.star.common.constant.RoleEnum.USER;

/**
 * 用户业务
 *
 * @Author: zzStar
 * @Date: 12-21-2020 16:00
 */
@Service
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements IService<UserAuth> {

    @Resource
    private BlogInfoServiceImpl blogInfoServiceImpl;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UserRoleMapper userRoleMapper;

    @Resource
    private UserAuthMapper userAuthMapper;

    @Resource
    private UserInfoMapper userInfoMapper;

    /**
     * @param username
     */
    public void sendCode(String username) {
        if (!checkEmail(username)) {
            throw new StarryException("请输入正确邮箱");
        }
        // 生成六位随机验证码
        StringBuilder code = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        EmailDTO email = EmailDTO.builder()
                .email(username)
                .subject("验证码")
                .content("您的验证码为 " + code.toString() + " 悄悄告诉你，有效期只有5分钟，不要跟别人说哦！").build();
        rabbitTemplate.convertAndSend(RabbitmqConst.EMAIL_EXCHANGE,
                "*", new Message(JSON.toJSONBytes(email),
                        new MessageProperties()));
        // 将验证码存入redis，设置过期时间为5分钟
        redisUtils.set(CODE_KEY + username, code, CODE_EXPIRE_TIME);
    }

    @Transactional(rollbackFor = StarryException.class)
    public void register(UserVO user) {
        if (checkUser(user)) {
            throw new StarryException("邮箱已被注册！");
        }
        // 新增用户信息
        UserInfo userInfo = UserInfo.builder()
                .nickname(DEFAULT_NICKNAME + IdWorker.getId())
                .avatar(DEFAULT_AVATAR)
                .email(user.getUsername())
                .avatar(blogInfoServiceImpl.getWebsiteConfig().getUserAvatar())
                .build();
        userInfoMapper.insert(userInfo);
        // 绑定用户角色
        saveUserRole(userInfo);
        // 新增用户账号
        UserAuth userAuth = UserAuth.builder()
                .userInfoId(userInfo.getId())
                .username(user.getUsername())
                .password(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                .loginType(EMAIL.getType()).build();
        userAuthMapper.insert(userAuth);
    }

    /**
     * 绑定用户角色
     *
     * @param userInfo 用户信息
     */
    @Transactional(rollbackFor = StarryException.class)
    public void saveUserRole(UserInfo userInfo) {
        UserRole userRole = UserRole.builder()
                .userId(userInfo.getId())
                .roleId(USER.getRoleId()).build();
        userRoleMapper.insert(userRole);
    }

    /**
     * @param user
     */
    @Transactional(rollbackFor = StarryException.class)
    public void updatePassword(UserVO user) {
        if (!checkUser(user)) {
            throw new StarryException("邮箱尚未注册！");
        }
        userAuthMapper.update(new UserAuth(), new LambdaUpdateWrapper<UserAuth>()
                .set(UserAuth::getPassword, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                .eq(UserAuth::getUsername, user.getUsername()));
    }

    /**
     * 检测邮箱是否合法
     *
     * @param username 用户名
     * @return 合法状态
     */
    private boolean checkEmail(String username) {
        String RULE_EMAIL = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$";
        // 正则表达式的模式 编译正则表达式
        Pattern p = Pattern.compile(RULE_EMAIL);
        // 正则表达式的匹配器
        Matcher m = p.matcher(username);
        // 进行正则匹配
        return m.matches();
    }

    /**
     * 校验用户数据是否合法
     *
     * @param user 用户数据
     * @return 合法状态
     */
    private Boolean checkUser(UserVO user) {
        if (!user.getCode().equals(redisUtils.get(CODE_KEY + user.getUsername()))) {
            throw new StarryException("验证码错误！");
        }
        UserAuth userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getUsername)
                .eq(UserAuth::getUsername, user.getUsername()));
        return Objects.nonNull(userAuth);
    }

}
