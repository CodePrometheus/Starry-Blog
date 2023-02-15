package com.star.rpc.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author: Starry
 * @Date: 02-15-2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JobRunVO {

    @ApiModelProperty(name = "任务id", value = "id", required = true, dataType = "Integer")
    private Integer id;

    @ApiModelProperty(name = "任务组别", value = "jobGroup", required = true, dataType = "String")
    private String jobGroup;

}
