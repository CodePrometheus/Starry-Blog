package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-16-2021 14:35
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {

    /**
     * id
     */
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
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 排序
     */
    private Integer orderNum;

    /**
     * 是否隐藏
     */
    private Integer isHidden;

    /**
     * 子菜单列表
     */
    private List<MenuDTO> children;

}
