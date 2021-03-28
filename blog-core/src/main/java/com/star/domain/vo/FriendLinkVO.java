package com.star.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 友链VO
 *
 * @Author: zzStar
 * @Date: 12-18-2020 17:36
 */
@Data
@ApiModel(description = "友链")
public class FriendLinkVO {
    /**
     * id
     */
    @ApiModelProperty(name = "categoryId", value = "友链id", dataType = "Integer")
    private Integer id;

    /**
     * 链接名
     */
    @NotBlank(message = "链接名不能为空")
    @ApiModelProperty(name = "linkName", value = "友链名", dataType = "String", required = true)
    private String linkName;

    /**
     * 链接头像
     */
    @NotBlank(message = "链接头像不能为空")
    @ApiModelProperty(name = "linkAvatar", value = "友链头像", dataType = "String", required = true)
    private String linkAvatar;

    /**
     * 链接地址
     */
    @NotBlank(message = "链接地址不能为空")
    @ApiModelProperty(name = "linkAddress", value = "友链头像", dataType = "String", required = true)
    private String linkAddress;

    /**
     * 介绍
     */
    @NotBlank(message = "链接介绍不能为空")
    @ApiModelProperty(name = "linkIntro", value = "友链头像", dataType = "String", required = true)
    private String linkIntro;

}
