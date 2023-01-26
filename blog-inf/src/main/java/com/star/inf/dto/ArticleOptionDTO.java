package com.star.inf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 文章选项
 *
 * @Author: zzStar
 * @Date: 12-19-2020 15:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleOptionDTO {

    /**
     * 文章标签列表
     */
    private List<TagDTO> tagDTOList;

    /**
     * 文章分类列表
     */
    private List<CategoryBackDTO> categoryDTOList;

}
