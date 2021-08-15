package com.star.core.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.constant.PathConst;
import com.star.common.exception.StarryException;
import com.star.common.tool.ImageUtil;
import com.star.common.tool.RedisUtil;
import com.star.core.entity.UserInfo;
import com.star.core.entity.UserRole;
import com.star.core.mapper.UserInfoMapper;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.EmailVO;
import com.star.core.vo.UserInfoVO;
import com.star.core.vo.UserRoleVO;
import com.star.core.service.UserInfoService;
import com.star.core.service.UserRoleService;
import com.star.core.dto.PageData;
import com.star.core.dto.UserInfoDTO;
import com.star.core.dto.UserOnlineDTO;
import com.star.core.util.UserUtil;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.star.common.constant.RedisConst.CODE_KEY;


/**
 * 用户信息业务
 *
 * @Author: zzStar
 * @Date: 12-20-2020 19:46
 */
@Service
@SuppressWarnings("unchecked")
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements UserInfoService {

    @Resource
    private SessionRegistry sessionRegistry;

    @Resource
    private RedisUtil redisUtil;

    @Resource
    private UserRoleService userRoleService;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUserInfo(UserInfoVO userInfoVO) {
        // 封装用户信息
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtil.getLoginUser().getUserInfoId())
                .nickname(userInfoVO.getNickname())
                .intro(userInfoVO.getIntro())
                .webSite(userInfoVO.getWebSite())
                .updateTime(new Date()).build();
        userInfoMapper.updateById(userInfo);
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public String updateUserAvatar(MultipartFile file) {
        String avatar = ImageUtil.upload(file, PathConst.AVATAR);
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtil.getLoginUser().getUserInfoId())
                .avatar(avatar).build();
        userInfoMapper.updateById(userInfo);
        return avatar;
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updateUserRole(UserRoleVO userRoleVO) {
        // 更新用户角色和昵称
        UserInfo userInfo = UserInfo.builder()
                .id(userRoleVO.getUserInfoId())
                .nickname(userRoleVO.getNickname()).build();
        userInfoMapper.updateById(userInfo);

        // 删除用户角色重新添加
        userRoleService.remove(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userRoleVO.getUserInfoId()));
        List<UserRole> userRoleList = userRoleVO.getRoleIdList().stream()
                .map(roleId -> UserRole.builder()
                        .roleId(roleId)
                        .userId(userRoleVO.getUserInfoId()).build())
                .collect(Collectors.toList());
        userRoleService.saveBatch(userRoleList);
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void updateUserDisable(Integer userInfoId, Integer isDisable) {
        // 更新用户禁用状态
        UserInfo userInfo = UserInfo.builder()
                .id(userInfoId)
                .isDisable(isDisable).build();
        userInfoMapper.updateById(userInfo);
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void saveUserEmail(EmailVO emailVO) {
        if (!emailVO.getCode().equals(redisUtil.get(CODE_KEY + emailVO.getEmail()))) {
            throw new StarryException("验证码错误");
        }
        UserInfo userInfo = UserInfo.builder()
                .id(UserUtil.getLoginUser().getUserInfoId())
                .email(emailVO.getEmail()).build();
        userInfoMapper.updateById(userInfo);
    }

    @Override
    public PageData<UserOnlineDTO> listOnlineUsers(ConditionVO conditionVO) {
        // 获取security在线session
        List<UserOnlineDTO> onlineUserList = sessionRegistry.getAllPrincipals().stream()
                .filter(item -> sessionRegistry.getAllSessions(item, false).size() > 0)
                .map(item -> JSON.parseObject(JSON.toJSONString(item), UserOnlineDTO.class))
                .sorted(Comparator.comparing(UserOnlineDTO::getLastLoginTime).reversed())
                .collect(Collectors.toList());
        // 分页
        int current = (conditionVO.getCurrent() - 1) * conditionVO.getSize();
        int size = onlineUserList.size() > conditionVO.getSize() ? current + conditionVO.getSize()
                : onlineUserList.size();
        List<UserOnlineDTO> subList = onlineUserList.subList((conditionVO.getCurrent() - 1) * conditionVO.getSize(), size);
        return new PageData<>(subList, onlineUserList.size());
    }

    @Override
    public void removeOnlineUser(Integer userInfoId) {
        List<Object> userInfoList = sessionRegistry.getAllPrincipals().stream().filter(item -> {
            UserInfoDTO userInfoDTO = (UserInfoDTO) item;
            return userInfoDTO.getUserInfoId().equals(userInfoId);
        }).collect(Collectors.toList());
        List<SessionInformation> list = new ArrayList<>();
        userInfoList.forEach(item -> list.addAll(sessionRegistry.getAllSessions(item, false)));
        // 注销
        list.forEach(SessionInformation::expireNow);
    }

}
