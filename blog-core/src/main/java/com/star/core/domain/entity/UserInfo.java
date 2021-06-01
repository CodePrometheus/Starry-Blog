package com.star.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.star.core.domain.vo.UserInfoVO;
import com.star.core.domain.vo.UserRoleVO;
import com.star.core.util.UserUtil;
import lombok.Data;

import java.util.Date;

/**
 * 用户信息
 *
 * @Author: zzStar
 * @Date: 12-16-2020 20:26
 */
@Data
@TableName("tb_user_info")
public class UserInfo {

    /**
     * 用户ID
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户角色
     */
    private String userRole;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 用户简介
     */
    private String intro;

    /**
     * 个人网站
     */
    private String webSite;

    /**
     * 是否禁言
     */
    private Integer isSilence;

    /**
     * 创建时间
     */
    private Date createTime;


    public UserInfo() {
        this.nickname = "用户" + System.currentTimeMillis();
        this.createTime = new Date();
    }

    public UserInfo(String nickname, String avatar) {
        this.nickname = nickname;
        this.avatar = avatar;
        this.createTime = new Date();
    }

    public UserInfo(UserInfoVO userInfoVO) {
        this.id = UserUtil.getLoginUser().getUserInfoId();
        this.nickname = userInfoVO.getNickname();
        this.intro = userInfoVO.getIntro();
        this.webSite = userInfoVO.getWebSite();
    }

    public UserInfo(String avatar) {
        this.id = UserUtil.getLoginUser().getUserInfoId();
        this.avatar = avatar;
    }

    public UserInfo(UserRoleVO userRoleVO) {
        this.id = userRoleVO.getUserInfoId();
        this.nickname = userRoleVO.getNickname();
        this.userRole = userRoleVO.getUserRole();
    }

    public UserInfo(Integer id, Integer isSilence) {
        this.id = id;
        this.isSilence = isSilence;
    }
}
