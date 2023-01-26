package com.star.inf.vo;

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
 * @Date: 08-15-2021 20:36
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "页面")
public class PageVO {

    /**
     * 页面id
     */
    @NotNull(message = "页面id不能为空")
    @ApiModelProperty(name = "id", value = "页面id", required = true, dataType = "Integer")
    private Integer id;

    /**
     * 页面名
     */
    @NotBlank(message = "页面名称不能为空")
    @ApiModelProperty(name = "pageName", value = "页面名称", required = true, dataType = "String")
    private String pageName;

    /**
     * 页面标签
     */
    @NotBlank(message = "页面标签不能为空")
    @ApiModelProperty(name = "pageLabel", value = "页面标签", required = true, dataType = "String")
    private String pageLabel;

    /**
     * 页面封面
     */
    @NotBlank(message = "页面封面不能为空")
    @ApiModelProperty(name = "pageCover", value = "页面封面", required = true, dataType = "String")
    private String pageCover;

}
