package com.star.admin.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.common.exception.StarryException;
import com.star.inf.dto.PageData;
import com.star.inf.dto.UserInfoDTO;
import com.star.inf.dto.UserOnlineDTO;
import com.star.inf.entity.UserInfo;
import com.star.inf.entity.UserRole;
import com.star.inf.mapper.UserInfoMapper;
import com.star.inf.service.UserRoleServiceImpl;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.UserDisableVO;
import com.star.inf.vo.UserRoleVO;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.star.common.tool.PageUtils.getLimitCurrent;
import static com.star.common.tool.PageUtils.getSize;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@Service
public class AdminUserInfoService {

    @Resource
    private UserRoleServiceImpl userRoleServiceImpl;

    @Resource
    private UserInfoMapper userInfoMapper;

    @Resource
    private SessionRegistry sessionRegistry;

    /**
     * 修改用户权限
     *
     * @param userRoleVO 用户权限
     */
    @Transactional(rollbackFor = StarryException.class)
    public void updateUserRole(UserRoleVO userRoleVO) {
        // 更新用户角色和昵称
        UserInfo userInfo = UserInfo.builder()
                .id(userRoleVO.getUserInfoId())
                .nickname(userRoleVO.getNickname()).build();
        userInfoMapper.updateById(userInfo);

        // 删除用户角色重新添加
        userRoleServiceImpl.remove(new LambdaQueryWrapper<UserRole>()
                .eq(UserRole::getUserId, userRoleVO.getUserInfoId()));
        List<UserRole> userRoleList = userRoleVO.getRoleIdList().stream()
                .map(roleId -> UserRole.builder()
                        .roleId(roleId)
                        .userId(userRoleVO.getUserInfoId()).build())
                .collect(Collectors.toList());
        userRoleServiceImpl.saveBatch(userRoleList);
    }

    /**
     * 修改用户禁用状态
     *
     * @param disableVO 用户禁用信息
     */
    @Transactional(rollbackFor = StarryException.class)
    public void updateUserDisable(UserDisableVO disableVO) {
        // 更新用户禁用状态
        UserInfo userInfo = UserInfo.builder()
                .id(disableVO.getId())
                .isDisable(disableVO.getIsDisable()).build();
        userInfoMapper.updateById(userInfo);
    }

    /**
     * 查看在线用户列表
     *
     * @param conditionVO
     * @return
     */
    public PageData<UserOnlineDTO> listOnlineUsers(ConditionVO conditionVO) {
        // 获取security在线session
        List<UserOnlineDTO> onlineUserList = sessionRegistry.getAllPrincipals().stream()
                .filter(item -> sessionRegistry.getAllSessions(item, false).size() > 0)
                .map(item -> JSON.parseObject(JSON.toJSONString(item), UserOnlineDTO.class))
                .filter(v -> StringUtils.isBlank(conditionVO.getKeywords()) ||
                        v.getNickname().contains(conditionVO.getKeywords()))
                .sorted(Comparator.comparing(UserOnlineDTO::getLastLoginTime).reversed())
                .collect(Collectors.toList());
        // 分页
        int fromIdx = getLimitCurrent().intValue();
        int size = getSize().intValue();
        int toIdx = onlineUserList.size() - fromIdx > size ? fromIdx + size : onlineUserList.size();

        List<UserOnlineDTO> onlineList = onlineUserList.subList(fromIdx, toIdx);
        return new PageData<>(onlineList, (long) onlineUserList.size());
    }

    /**
     * 下线用户
     *
     * @param userInfoId 用户信息id
     */
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
