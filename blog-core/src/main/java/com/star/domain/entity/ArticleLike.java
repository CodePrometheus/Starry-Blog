package com.star.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 文章点赞记录
 *
 * @Author: zzStar
 * @Date: 12-16-2020 22:53
 */
@Data
@TableName("tb_article_like")
public class ArticleLike {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 点赞用户
     */
    private Integer userId;

    /**
     * 点赞文章
     */
    private Integer articleId;


}
