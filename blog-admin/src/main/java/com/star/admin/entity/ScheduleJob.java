package com.star.admin.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @Author: Starry
 * @Date: 02-15-2023
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("tb_job")
public class ScheduleJob {

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 任务名
     */
    private String jobName;

    /**
     * 任务分组
     */
    private String jobGroup;

    /**
     * 调用方法 目标字符串
     */
    private String invokeTarget;

    /**
     * cron 表达式
     */
    private String cronExpression;

    /**
     * 错误策略
     */
    private Integer misfirePolicy;

    /**
     * 是否并发
     */
    private Integer concurrent;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 备注
     */
    private String remark;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
