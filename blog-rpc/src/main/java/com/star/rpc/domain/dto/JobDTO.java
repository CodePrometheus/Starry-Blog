package com.star.rpc.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @Author: Starry
 * @Date: 02-15-2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class JobDTO {

    private Integer id;

    private String jobName;

    private String jobGroup;

    private String invokeTarget;

    private String cronExpression;

    private Integer misfirePolicy;

    private Integer concurrent;

    private Integer status;

    private LocalDateTime createTime;

    private String remark;

    /**
     * 下一次调度时间
     */
    private Date nextValidTime;

}
