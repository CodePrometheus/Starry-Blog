package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.domain.entity.UserInfo;
import com.star.core.domain.vo.UserInfoVO;
import com.star.core.domain.vo.UserRoleVO;
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
     * 修改用户禁言状态
     *
     * @param userInfoId 用户信息id
     * @param isSilence  禁言状态
     */
    void updateUserSilence(Integer userInfoId, Integer isSilence);

}
