package com.star.core.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 归档VO
 *
 * @Author: zzStar
 * @Date: 12-18-2020 17:25
 */
@Data
@ApiModel(description = "分类")
public class CategoryVO {

    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "分类id", dataType = "Integer")
    private Integer id;

    /**
     * 分类名
     */
    @NotBlank(message = "分类名不能为空")
    @ApiModelProperty(name = "categoryName", value = "分类名", required = true, dataType = "String")
    private String categoryName;

}
