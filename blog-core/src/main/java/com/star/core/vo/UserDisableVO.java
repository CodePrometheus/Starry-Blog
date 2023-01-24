package com.star.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zzStar
 * @Date: 08-16-2021 21:33
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "用户禁用状态")
public class UserDisableVO {

    /**
     * id
     */
    @NotNull(message = "用户id不能为空")
    @ApiModelProperty(name = "id", value = "用户id不能为空", required = true, dataType = "Integer")
    private Integer id;

    /**
     * 置顶状态
     */
    @NotNull(message = "用户禁用状态状态不能为空")
    @ApiModelProperty(name = "isDisable", value = "用户禁用状态状态不能为空", required = true, dataType = "Integer")
    private Integer isDisable;

}
