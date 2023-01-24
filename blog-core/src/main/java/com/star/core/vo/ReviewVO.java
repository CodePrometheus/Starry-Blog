package com.star.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 08-15-2021 23:34
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "审核")
public class ReviewVO {

    /**
     * id列表
     */
    @NotNull(message = "id不能为空")
    @ApiModelProperty(name = "idList", value = "id列表", required = true, dataType = "List<Integer>")
    private List<Integer> idList;

    /**
     * 状态值
     */
    @NotNull(message = "状态值不能为空")
    @ApiModelProperty(name = "isDelete", value = "删除状态", required = true, dataType = "Integer")
    private Integer isReview;

}
