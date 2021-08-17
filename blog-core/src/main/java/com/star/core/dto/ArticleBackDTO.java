package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * 后台文章VO
 *
 * @Author: zzStar
 * @Date: 12-19-2020 16:50
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArticleBackDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 文章封面
     */
    private String articleCover;

    /**
     * 发表时间
     */
    private LocalDateTime createTime;

    /**
     * 点赞量
     */
    private Integer likeCount;

    /**
     * 浏览量
     */
    private Integer viewsCount;

    /**
     * 文章分类名
     */
    private String categoryName;

    /**
     * 文章标签
     */
    private List<TagDTO> tagList;

    /**
     * 文章类型
     */
    private Integer type;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 文章状态
     */
    private Integer status;

}
