package com.star.inf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zzStar
 * @Date: 2021/8/20
 * @Description:
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "角色禁用状态")
public class RoleDisableVO {

    /**
     * id
     */
    @NotNull(message = "角色id不能为空")
    @ApiModelProperty(name = "id", value = "角色id不能为空", required = true, dataType = "Integer")
    private Integer id;

    /**
     * 置顶状态
     */
    @NotNull(message = "角色禁用状态状态不能为空")
    @ApiModelProperty(name = "isDisable", value = "角色禁用状态状态不能为空", required = true, dataType = "Integer")
    private Integer isDisable;

}
