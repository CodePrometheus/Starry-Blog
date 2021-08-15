package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zzStar
 * @Date: 08-15-2021 22:47
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryOptionDTO {

    /**
     * 分类id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String categoryName;

}
