package com.star.core.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 文章标签
 *
 * @Author: zzStar
 * @Date: 12-18-2020 17:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@TableName("tb_article_tag")
public class ArticleTag {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 文章id
     */
    private Integer articleId;

    /**
     * 标签id
     */
    private Integer tagId;

}
