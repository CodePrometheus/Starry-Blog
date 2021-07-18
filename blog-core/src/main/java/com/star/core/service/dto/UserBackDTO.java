package com.star.core.service.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

/**
 * 后台用户列表
 *
 * @Author: zzStar
 * @Date: 12-21-2020 16:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserBackDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 用户信息id
     */
    private Integer userInfoId;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 登录类型
     */
    private Integer loginType;

    /**
     * 用户登录ip
     */
    private String ipAddr;

    /**
     * ip来源
     */
    private String ipSource;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最近登录时间
     */
    private Date lastLoginTime;

    /**
     * 用户评论状态
     */
    private Integer isDisable;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 用户角色
     */
    private List<UserRoleDTO> roleList;

}
