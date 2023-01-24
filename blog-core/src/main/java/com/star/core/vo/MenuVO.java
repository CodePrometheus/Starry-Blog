package com.star.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zzStar
 * @Date: 08-16-2021 20:07
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "菜单")
public class MenuVO {

    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "菜单id", dataType = "Integer")
    private Integer id;

    /**
     * 菜单名
     */
    @NotBlank(message = "菜单名不能为空")
    @ApiModelProperty(name = "name", value = "菜单名", dataType = "String")
    private String name;

    /**
     * icon
     */
    @NotBlank(message = "菜单icon不能为空")
    @ApiModelProperty(name = "icon", value = "菜单icon", dataType = "String")
    private String icon;

    /**
     * 路径
     */
    @NotBlank(message = "路径不能为空")
    @ApiModelProperty(name = "path", value = "路径", dataType = "String")
    private String path;

    /**
     * 组件
     */
    @NotBlank(message = "组件不能为空")
    @ApiModelProperty(name = "component", value = "组件", dataType = "String")
    private String component;

    /**
     * 排序
     */
    @NotNull(message = "排序不能为空")
    @ApiModelProperty(name = "orderNum", value = "排序", dataType = "Integer")
    private Integer orderNum;

    /**
     * 父id
     */
    @ApiModelProperty(name = "parentId", value = "父id", dataType = "Integer")
    private Integer parentId;

    /**
     * 是否隐藏
     */
    @ApiModelProperty(name = "isHidden", value = "是否隐藏", dataType = "Integer")
    private Integer isHidden;

}
