package com.star.core.dto;

import io.swagger.annotations.ApiModel;
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
@ApiModel("后台博客信息")
public class BlogBackInfoDTO {

    /**
     * 访问量
     */
    private Integer viewsCount;

    /**
     * 留言量
     */
    private Long messageCount;

    /**
     * 用户量
     */
    private Long userCount;

    /**
     * 文章量
     */
    private Long articleCount;

    /**
     * 分类统计
     */
    private List<CategoryDTO> categoryList;

    /**
     * 一周用户量集合
     */
    private List<UniqueViewDTO> uniqueViewList;

    /**
     * 文章浏览量排行
     */
    private List<ArticleRankDTO> articleRankList;

    /**
     * 标签列表
     */
    private List<TagDTO> tagList;

    /**
     * 文章统计列表
     */
    private List<ArticleStatisticsDTO> articleStatisticsList;

}
