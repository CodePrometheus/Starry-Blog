package com.star.core.service.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 后台文章VO
 *
 * @Author: zzStar
 * @Date: 12-19-2020 16:50
 */
@Data
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
     * 发表时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

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
    private List<TagDTO> tagDTOList;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 是否为草稿
     */
    private Integer isDraft;

    /**
     * 是否删除
     */
    private Integer isDelete;

}
