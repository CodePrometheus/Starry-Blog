package com.star.admin.domain.vo;

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
public class JobSearchVO {

    @ApiModelProperty(name = "任务名称", value = "jobName", required = true, dataType = "String")
    private String jobName;

    @ApiModelProperty(name = "任务组别", value = "jobGroup", required = true, dataType = "String")
    private String jobGroup;

    @ApiModelProperty(name = "任务状态", value = "status", required = true, dataType = "Integer")
    private Integer status;

}
