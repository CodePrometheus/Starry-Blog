package com.star.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: zzStar
 * @Date: 2022/1/12 11:12 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_moment")
public class Moment {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 内容
     */
    private String momentContent;

    /**
     * 点赞数
     */
    @TableField("`like`")
    private Integer like;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 状态码
     */
    private Integer isDelete;

    /**
     * 文章状态 1.公开 2.私密 3.草稿箱
     */
    private Integer status;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 修改时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

}
