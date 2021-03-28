package com.star.service.dto;

import lombok.Data;

/**
 * 归档列表
 *
 * @Author: zzStar
 * @Date: 12-19-2020 14:10
 */
@Data
public class CategoryDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String categoryName;

    /**
     * 分类下的文章数量
     */
    private Long articleCount;

}
