package com.star.core.vo;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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
    private Integer id;

    /**
     * 置顶状态
     */
    @NotNull(message = "角色禁用状态状态不能为空")
    private Integer isDisable;

}
