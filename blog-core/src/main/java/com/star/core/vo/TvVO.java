package com.star.core.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author: zzStar
 * @Date: 04-10-2021 16:26
 */
@Data
public class TvVO {

    @ApiModelProperty(value = "id")
    private Integer id;

    @ApiModelProperty(value = "电视名")
    private String name;

    @ApiModelProperty(value = "类型")
    private String type;

    @ApiModelProperty(value = "流地址")
    private String url;

}
