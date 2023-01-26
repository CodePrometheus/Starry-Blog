package com.star.inf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 各种查询的条件
 *
 * @Author: zzStar
 * @Date: 12-19-2020 14:01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "查询条件")
public class ConditionVO {

    /**
     * 分类id
     */
    @ApiModelProperty(name = "categoryId", value = "分类id", dataType = "Integer")
    private Integer categoryId;

    /**
     * 标签id
     */
    @ApiModelProperty(name = "tagId", value = "标签id", dataType = "Integer")
    private Integer tagId;

    /**
     * 登录类型
     */
    @ApiModelProperty(name = "type", value = "登录类型", dataType = "Integer")
    private Integer loginType;

    /**
     * 类型
     */
    @ApiModelProperty(name = "type", value = "类型", dataType = "Integer")
    private Integer type;

    /**
     * 状态
     */
    @ApiModelProperty(name = "status", value = "状态", dataType = "Integer")
    private Integer status;

    /**
     * 当前页码
     */
    @ApiModelProperty(name = "current", value = "当前页码", required = true, dataType = "Long")
    private Long current;

    /**
     * 显示数量
     */
    @ApiModelProperty(name = "size", value = "显示数量", required = true, dataType = "Long")
    private Long size;

    /**
     * 搜索内容
     */
    @ApiModelProperty(name = "keywords", value = "搜索内容", required = true, dataType = "String")
    private String keywords;

    /**
     * 是否删除
     */
    @ApiModelProperty(name = "isDelete", value = "是否删除", dataType = "Integer")
    private Integer isDelete;

    /**
     * 是否审核
     */
    @ApiModelProperty(name = "isReview", value = "是否审核", dataType = "Integer")
    private Integer isReview;

    /**
     * 开始时间
     */
    @ApiModelProperty(name = "startTime", value = "开始时间", dataType = "LocalDateTime")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @ApiModelProperty(name = "endTime", value = "结束时间", dataType = "LocalDateTime")
    private LocalDateTime endTime;

}
