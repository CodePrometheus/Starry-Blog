package com.star.inf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * 用户信息vo
 *
 * @Author: zzStar
 * @Date: 12-16-2020 22:24
 */
@Data
@ApiModel(description = "用户信息对象")
public class UserInfoVO {

    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty(name = "nickname", value = "昵称", dataType = "String")
    private String nickname;

    /**
     * 用户简介
     */
    @ApiModelProperty(name = "intro", value = "介绍", dataType = "String")
    private String intro;

    /**
     * 个人网站
     */
    @ApiModelProperty(name = "webSite", value = "个人网站", dataType = "String")
    private String webSite;
}