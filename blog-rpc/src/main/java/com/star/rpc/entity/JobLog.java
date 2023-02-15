package com.star.rpc.entity;

import com.baomidou.mybatisplus.annotation.*;
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
@TableName("tb_job_log")
public class JobLog {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    private Integer jobId;

    /**
     * 任务名称
     */
    private String jobName;

    /**
     * 任务组名
     */
    private String jobGroup;

    /**
     * 调用方法 目标字符串
     */
    private String invokeTarget;

    /**
     * 日志信息
     */
    private String jobMessage;

    /**
     * 执行状态
     */
    private Integer status;

    /**
     * 调度日志详细
     */
    private String exceptionInfo;

    private Date startTime;

    private Date endTime;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

}
