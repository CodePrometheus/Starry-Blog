package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 后台博客信息
 *
 * @Author: zzStar
 * @Date: 12-19-2020 21:52
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogBackInfoDTO {
    /**
     * 访问量
     */
    private Integer viewsCount;

    /**
     * 留言量
     */
    private Integer messageCount;

    /**
     * 用户量
     */
    private Integer userCount;

    /**
     * 文章量
     */
    private Integer articleCount;

    /**
     * 分类统计
     */
    private List<CategoryDTO> categoryDTOList;

    /**
     * 一周用户量集合
     */
    private List<UniqueViewDTO> uniqueViewDTOList;

    /**
     * 文章浏览量排行
     */
    private List<ArticleRankDTO> articleRankDTOList;

}
