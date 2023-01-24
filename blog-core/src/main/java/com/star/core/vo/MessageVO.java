package com.star.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * 留言VO
 *
 * @Author: zzStar
 * @Date: 12-18-2020 17:39
 */
@Data
@ApiModel(description = "留言")
public class MessageVO {

    /**
     * 昵称
     */
    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty(name = "nickname", value = "昵称", required = true, dataType = "String")
    private String nickname;

    /**
     * 头像
     */
    @NotBlank(message = "头像不能为空")
    @ApiModelProperty(name = "avatar", value = "头像", required = true, dataType = "String")
    private String avatar;

    /**
     * 留言内容
     */
    @NotBlank(message = "留言内容不能为空")
    @ApiModelProperty(name = "messageContent", value = "留言内容", required = true, dataType = "String")
    private String messageContent;

    /**
     * 弹幕速度
     */
    @NotNull(message = "弹幕速度不能为空")
    @ApiModelProperty(name = "time", value = "弹幕速度", required = true, dataType = "Integer")
    private Integer time;

}
