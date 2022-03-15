package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 2022/1/12 11:33 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MomentDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 内容
     */
    private String momentContent;

    /**
     * 图片
     */
    private String images;

    /**
     * 图片列表
     */
    private List<String> imgList;

    /**
     * 点赞数
     */
    private Integer likeCount;

    /**
     * 评论量
     */
    private Long commentCount;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 是否删除
     */
    private Integer isDelete;

    /**
     * 状态 1.公开 2.私密 3.草稿箱
     */
    private Integer status;

    /**
     * 发表时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

}
