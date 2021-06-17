package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.service.dto.UserBackDTO;
import com.star.core.domain.entity.UserAuth;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.domain.vo.PasswordVO;
import com.star.core.domain.vo.UserVO;
import com.star.core.service.dto.PageDTO;

/**
 * @author zzStar
 */
public interface UserAuthService extends IService<UserAuth> {

    /**
     * 发送邮箱验证码
     *
     * @param username 邮箱号
     */
    void sendCode(String username);

    /**
     * 用户注册
     *
     * @param user 用户对象
     */
    void saveUser(UserVO user);

    /**
     * 修改密码
     *
     * @param user 用户对象
     */
    void updatePassword(UserVO user);

    /**
     * 修改管理员密码
     *
     * @param passwordVO 密码对象
     */
    void updateAdminPassword(PasswordVO passwordVO);

    /**
     * 查询后台用户列表
     *
     * @param condition 条件
     * @return 用户列表
     */
    PageDTO<UserBackDTO> listUserBackDTO(ConditionVO condition);

}
