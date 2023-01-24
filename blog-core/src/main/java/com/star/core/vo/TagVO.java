package com.star.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签VO
 *
 * @Author: zzStar
 * @Date: 12-18-2020 17:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "标签对象")
public class TagVO {

    /**
     * id
     */
    @ApiModelProperty(name = "id", value = "标签id", dataType = "Integer")
    private Integer id;

    /**
     * 标签名
     */
    @NotBlank(message = "标签名不能为空")
    @ApiModelProperty(name = "categoryName", value = "标签名", required = true, dataType = "String")
    private String tagName;

}
