package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.domain.entity.UserInfo;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.domain.vo.EmailVO;
import com.star.core.domain.vo.UserInfoVO;
import com.star.core.domain.vo.UserRoleVO;
import com.star.core.service.dto.PageDTO;
import com.star.core.service.dto.UserOnlineDTO;
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
     * @param userInfoId 用户信息id
     * @param isDisable  禁用状态
     */
    void updateUserDisable(Integer userInfoId, Integer isDisable);

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
    PageDTO<UserOnlineDTO> listOnlineUsers(ConditionVO conditionVO);

}
