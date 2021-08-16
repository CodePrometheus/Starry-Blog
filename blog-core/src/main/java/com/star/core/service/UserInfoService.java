package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.dto.PageData;
import com.star.core.dto.UserOnlineDTO;
import com.star.core.entity.UserInfo;
import com.star.core.vo.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author zzStar
 */
public interface UserInfoService extends IService<UserInfo> {

    /**
     * 修改用户资料
     *
     * @param userInfoVO 用户资料
     */
    void updateUserInfo(UserInfoVO userInfoVO);

    /**
     * 修改用户头像
     *
     * @param file 头像图片
     * @return 头像OSS地址
     */
    String updateUserAvatar(MultipartFile file);

    /**
     * 修改用户权限
     *
     * @param userRoleVO 用户权限
     */
    void updateUserRole(UserRoleVO userRoleVO);

    /**
     * 修改用户禁用状态
     *
     * @param userDisableVO 用户禁用信息
     */
    void updateUserDisable(UserDisableVO userDisableVO);

    /**
     * 绑定用户邮箱
     *
     * @param emailVO
     */
    void saveUserEmail(EmailVO emailVO);

    /**
     * 查看在线用户列表
     *
     * @param conditionVO
     * @return
     */
    PageData<UserOnlineDTO> listOnlineUsers(ConditionVO conditionVO);

    /**
     * 下线用户
     *
     * @param userInfoId 用户信息id
     */
    void removeOnlineUser(Integer userInfoId);

}
