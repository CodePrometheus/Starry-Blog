package com.star.core.service.dto;

import lombok.Data;

/**
 * 后台分类列表
 *
 * @Author: zzStar
 * @Date: 12-19-2020 15:23
 */
@Data
public class CategoryBackDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String categoryName;
}
