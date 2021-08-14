package com.star.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户权限vo
 *
 * @Author: zzStar
 * @Date: 12-16-2020 22:27
 */
@Data
@Builder
@ApiModel(description = "用户权限")
public class UserRoleVO {
    /**
     * 用户昵称
     */
    @NotNull(message = "id不能为空")
    @ApiModelProperty(name = "userInfoId", value = "用户信息id", dataType = "Integer")
    private Integer userInfoId;

    /**
     * 用户昵称
     */
    @NotBlank(message = "昵称不能为空")
    @ApiModelProperty(name = "nickname", value = "昵称", dataType = "String")
    private String nickname;

    /**
     * 用户角色
     */
    @NotNull(message = "用户角色不能为空")
    @ApiModelProperty(name = "roleList", value = "角色id集合", dataType = "List<Integer>")
    private List<Integer> roleIdList;

}
