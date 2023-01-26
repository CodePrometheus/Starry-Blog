package com.star.inf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 后台分类列表
 *
 * @Author: zzStar
 * @Date: 12-19-2020 15:23
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryBackDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 分类名
     */
    private String categoryName;

    /**
     * 文章数量
     */
    private Integer articleCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
