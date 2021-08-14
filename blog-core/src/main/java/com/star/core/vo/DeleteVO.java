package com.star.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 逻辑删除
 *
 * @Author: zzStar
 * @Date: 12-19-2020 13:33
 */
@Data
@ApiModel(description = "逻辑删除")
public class DeleteVO {

    /**
     * id列表
     */
    @ApiModelProperty(name = "idList", value = "id列表", required = true, dataType = "List<Integer>")
    private List<Integer> idList;

    /**
     * 状态值
     */
    @ApiModelProperty(name = "isDelete", value = "删除状态", required = true, dataType = "Integer")
    private Integer isDelete;

}
