package com.star.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.IpUtil;
import com.star.common.tool.RedisUtil;
import com.star.core.config.RabbitConfig;
import com.star.core.domain.entity.UserAuth;
import com.star.core.domain.entity.UserInfo;
import com.star.core.domain.entity.UserRole;
import com.star.core.domain.mapper.RoleMapper;
import com.star.core.domain.mapper.UserAuthMapper;
import com.star.core.domain.mapper.UserInfoMapper;
import com.star.core.domain.mapper.UserRoleMapper;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.domain.vo.PasswordVO;
import com.star.core.domain.vo.UserVO;
import com.star.core.service.UserAuthService;
import com.star.core.service.dto.EmailDTO;
import com.star.core.service.dto.PageDTO;
import com.star.core.service.dto.UserBackDTO;
import com.star.core.service.dto.UserInfoDTO;
import com.star.core.util.UserUtil;
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

import static com.star.common.constant.CommonConst.DEFAULT_AVATAR;
import static com.star.common.constant.CommonConst.DEFAULT_NICKNAME;
import static com.star.common.constant.LoginTypeConst.EMAIL;
import static com.star.common.constant.RedisConst.*;
import static com.star.common.constant.RoleEnum.USER;
import static com.star.core.util.UserUtil.convertLoginUser;

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
        EmailDTO emailDTO = EmailDTO.builder()
                .email(username)
                .subject("验证码")
                .content("您的验证码为 " + code.toString() + " 悄悄告诉你，有效期只有5分钟，不要跟别人说哦！").build();
        rabbitTemplate.convertAndSend(RabbitConfig.EMAIL_EXCHANGE,
                "*", new Message(JSON.toJSONBytes(emailDTO),
                        new MessageProperties()));
        // 将验证码存入redis，设置过期时间为5分钟
        redisUtil.set(CODE_KEY + username, code, CODE_EXPIRE_TIME);
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void saveUser(UserVO user) {
        if (checkUser(user)) {
            throw new StarryException("邮箱已被注册！");
        }
        // 新增用户信息
        UserInfo userInfo = UserInfo.builder()
                .nickname(DEFAULT_NICKNAME)
                .avatar(DEFAULT_AVATAR)
                .createTime(new Date())
                .email(user.getUsername()).build();
        userInfoMapper.insert(userInfo);
        // 绑定用户角色
        saveUserRole(userInfo);
        // 新增用户账号
        UserAuth userAuth = UserAuth.builder()
                .userInfoId(userInfo.getId())
                .username(user.getUsername())
                .password(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()))
                .loginType(EMAIL.getType())
                .createTime(new Date()).build();
        userAuthMapper.insert(userAuth);
    }

    /**
     * 绑定用户角色
     *
     * @param userInfo 用户信息
     */
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
    public PageDTO<UserBackDTO> listUserBackDTO(ConditionVO condition) {
        // 转换页码
        condition.setCurrent((condition.getCurrent() - 1) * condition.getSize());
        // 获取后台用户数量
        Integer count = userAuthMapper.countUser(condition);
        if (count == 0) {
            return new PageDTO<>();
        }
        // 获取后台用户列表
        List<UserBackDTO> userBackDTOList = userAuthMapper.listUsers(condition);
        return new PageDTO<>(userBackDTOList, count);
    }


    /**
     * 获取本地第三方登录信息
     *
     * @param user 用户对象
     * @return 用户登录信息
     */
    private UserInfoDTO getUserInfoDTO(UserAuth user) {
        // 更新登录时间，ip
        String ipAddr = IpUtil.getIpAddr(request);
        String ipSource = IpUtil.getIpSource(ipAddr);
        // update
        userAuthMapper.update(new UserAuth(), new LambdaUpdateWrapper<UserAuth>()
                .set(UserAuth::getIpAddr, ipAddr)
                .set(UserAuth::getIpSource, ipSource)
                .set(UserAuth::getLastLoginTime, new Date())
                .eq(UserAuth::getId, user.getId()));
        // 查询账号对应的信息
        UserInfo userInfo = userInfoMapper.selectOne(new LambdaQueryWrapper<UserInfo>()
                .select(UserInfo::getId, UserInfo::getEmail, UserInfo::getNickname,
                        UserInfo::getAvatar, UserInfo::getIntro, UserInfo::getWebSite, UserInfo::getIsDisable)
                .eq(UserInfo::getId, user.getUserInfoId()));

        // 查询账号点赞信息包括文章和评论
        Set<Integer> articleLikeSet = (Set) redisUtil.hGet(ARTICLE_USER_LIKE, userInfo.getId().toString());
        Set<Integer> commentLikeSet = (Set) redisUtil.hGet(COMMENT_USER_LIKE, userInfo.getId().toString());
        // 查询账号角色消息
        List<String> roleList = roleMapper.listRolesByUserInfoId(userInfo.getId());
        // 封装登录信息
        return convertLoginUser(user, userInfo, roleList, articleLikeSet, commentLikeSet, request);
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

}
