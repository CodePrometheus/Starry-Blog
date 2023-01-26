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
 * @Date: 08-14-2021 22:06
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "修改置顶")
public class TopVO {

    /**
     * id
     */
    @NotNull(message = "id不能为空")
    @ApiModelProperty(name = "id", value = "id不能为空", dataType = "Integer")
    private Integer id;

    /**
     * 置顶状态
     */
    @NotNull(message = "置顶状态不能为空")
    @ApiModelProperty(name = "isTop", value = "置顶状态不能为空", dataType = "Integer")
    private Integer isTop;

}
