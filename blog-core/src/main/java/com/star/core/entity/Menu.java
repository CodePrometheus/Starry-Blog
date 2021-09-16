package com.star.core.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 12:37
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_menu")
public class Menu {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 菜单名
     */
    private String name;

    /**
     * 路径
     */
    private String path;

    /**
     * 组件
     */
    private String component;

    /**
     * icon
     */
    private String icon;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.UPDATE)
    private LocalDateTime updateTime;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 父id
     */
    private Integer parentId;

    /**
     * 是否隐藏
     */
    private Integer isHidden;

}
