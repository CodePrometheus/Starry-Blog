package com.star.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.RedisUtil;
import com.star.core.config.RabbitConfig;
import com.star.core.dto.EmailDTO;
import com.star.core.dto.PageData;
import com.star.core.dto.UserAreaDTO;
import com.star.core.dto.UserBackDTO;
import com.star.core.entity.UserAuth;
import com.star.core.entity.UserInfo;
import com.star.core.entity.UserRole;
import com.star.core.mapper.RoleMapper;
import com.star.core.mapper.UserAuthMapper;
import com.star.core.mapper.UserInfoMapper;
import com.star.core.mapper.UserRoleMapper;
import com.star.core.service.BlogInfoService;
import com.star.core.service.UserAuthService;
import com.star.core.util.PageUtils;
import com.star.core.util.UserUtil;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.PasswordVO;
import com.star.core.vo.UserVO;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.DEFAULT_AVATAR;
import static com.star.common.constant.CommonConst.DEFAULT_NICKNAME;
import static com.star.common.constant.LoginTypeConst.EMAIL;
import static com.star.common.constant.RedisConst.*;
import static com.star.common.constant.RoleEnum.USER;
import static com.star.common.enums.UserAreaTypeEnum.getUserAreaType;

/**
 * 用户业务
 *
 * @Author: zzStar
 * @Date: 12-21-2020 16:00
 */
@Service
@SuppressWarnings("all")
public class UserAuthServiceImpl extends ServiceImpl<UserAuthMapper, UserAuth> implements UserAuthService {

    @Resource
    private BlogInfoService blogInfoService;
    @Resource
    private RabbitTemplate rabbitTemplate;
    @Resource
    private RedisUtil redisUtil;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;
    @Resource
    private JavaMailSender javaMailSender;
    @Resource
    private UserAuthMapper userAuthMapper;
    @Resource
    private UserInfoMapper userInfoMapper;
    @Resource
    private RestTemplate restTemplate;
    @Resource
    private HttpServletRequest request;

    @Override
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
        rabbitTemplate.convertAndSend(RabbitConfig.EMAIL_EXCHANGE,
                "*", new Message(JSON.toJSONBytes(email),
                        new MessageProperties()));
        // 将验证码存入redis，设置过期时间为5分钟
        redisUtil.set(CODE_KEY + username, code, CODE_EXPIRE_TIME);
    }

    @Override
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
                .avatar(blogInfoService.getWebsiteConfig().getUserAvatar())
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
    private void saveUserRole(UserInfo userInfo) {
        UserRole userRole = UserRole.builder()
                .userId(userInfo.getId())
                .roleId(USER.getRoleId()).build();
        userRoleMapper.insert(userRole);
    }


    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updatePassword(UserVO user) {
        if (!checkUser(user)) {
            throw new StarryException("邮箱尚未注册！");
        }
        userAuthMapper.update(new UserAuth(), new LambdaUpdateWrapper<UserAuth>()
                .set(UserAuth::getPassword, BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                .eq(UserAuth::getUsername, user.getUsername()));
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updateAdminPassword(PasswordVO passwordVO) {
        // 查询旧密码是否正确
        UserAuth userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .eq(UserAuth::getId, UserUtil.getLoginUser().getId()));
        if (Objects.nonNull(userAuth) && BCrypt.checkpw(passwordVO.getOldPassword(), userAuth.getPassword())) {
            UserAuth user = UserAuth.builder()
                    .id(UserUtil.getLoginUser().getId())
                    .password(BCrypt.hashpw(passwordVO.getNewPassword(), BCrypt.gensalt())).build();
            userAuthMapper.updateById(user);
        } else {
            throw new StarryException("原密码不正确");
        }
    }

    @Override
    public PageData<UserBackDTO> listUserBack(ConditionVO condition) {
        // 获取后台用户数量
        Long count = userAuthMapper.countUser(condition);
        if (count == 0) {
            return new PageData<>();
        }
        // 获取后台用户列表
        List<UserBackDTO> userBackList = userAuthMapper.listUsers(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        return new PageData<>(userBackList, count);
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
        if (!user.getCode().equals(redisUtil.get(CODE_KEY + user.getUsername()))) {
            throw new StarryException("验证码错误！");
        }
        UserAuth userAuth = userAuthMapper.selectOne(new LambdaQueryWrapper<UserAuth>()
                .select(UserAuth::getUsername)
                .eq(UserAuth::getUsername, user.getUsername()));
        return Objects.nonNull(userAuth);
    }

    @Override
    public List<UserAreaDTO> listUserAreas(ConditionVO condition) {
        List<UserAreaDTO> userAreaList = new ArrayList<>();
        switch (Objects.requireNonNull(getUserAreaType(condition.getType()))) {
            case USER:
                // 查询注册用户区域分布
                Object userArea = redisUtil.get(USER_AREA);
                if (Objects.nonNull(userArea)) {
                    userAreaList = JSON.parseObject(userArea.toString(), List.class);
                }
                return userAreaList;
            case VISITOR:
                // 查询游客区域分布
                Map<String, Object> visitorArea = redisUtil.hGetAll(VISITOR_AREA);
                if (Objects.nonNull(visitorArea)) {
                    userAreaList = visitorArea.entrySet().stream()
                            .map(v -> UserAreaDTO.builder()
                                    .name(v.getKey())
                                    .value(Long.valueOf(v.getValue().toString())).build())
                            .collect(Collectors.toList());
                }
                return userAreaList;
            default:
                break;
        }
        return userAreaList;
    }

}
