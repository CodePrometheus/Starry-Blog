package com.star.admin.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @Author: Starry
 * @Date: 02-19-2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JobLogSearchVO {

    @ApiModelProperty(name = "任务Id", value = "jobId", dataType = "Integer")
    private Integer jobId;

    @ApiModelProperty(name = "任务名称", value = "jobName", dataType = "String")
    private String jobName;

    @ApiModelProperty(name = "任务的组别", value = "jobGroup", dataType = "String")
    private String jobGroup;

    @ApiModelProperty(name = "任务状态", value = "status", dataType = "Integer")
    private Integer status;

    @ApiModelProperty(name = "开始时间", value = "startTime", dataType = "String")
    private String startTime;

    @ApiModelProperty(name = "结束时间", value = "endTime", dataType = "String")
    private String endTime;

}
