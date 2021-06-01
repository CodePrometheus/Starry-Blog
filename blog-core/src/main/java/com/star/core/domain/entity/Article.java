package com.star.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.star.core.domain.vo.ArticleVO;
import com.star.core.util.UserUtil;
import lombok.Data;

import java.util.Date;

/**
 * 文章实体
 *
 * @Author: zzStar
 * @Date: 12-16-2020 20:10
 */
@Data
@TableName("tb_article")
public class Article {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 作者
     */
    private Integer userId;

    /**
     * 文章分类
     */
    private Integer categoryId;

    /**
     * 文章缩略图
     */
    private String articleCover;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 内容
     */
    private String articleContent;

    /**
     * 发表时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 是否为草稿
     */
    private Integer isDraft;

    /**
     * 状态码
     */
    private Integer isDelete;

    public Article(ArticleVO articleVO) {
        this.id = articleVO.getId();
        this.userId = UserUtil.getLoginUser().getUserInfoId();
        this.categoryId = articleVO.getCategoryId();
        this.articleCover = articleVO.getArticleCover();
        this.articleTitle = articleVO.getArticleTitle();
        this.articleContent = articleVO.getArticleContent();
        this.createTime = this.id == null ? new Date() : null;
        this.updateTime = this.id != null ? new Date() : null;
        this.isTop = articleVO.getIsTop();
        this.isDraft = articleVO.getIsDraft();
    }

    public Article(Integer id) {
        this.id = id;
        this.isTop = 0;
    }

    public Article(Integer id, Integer isTop) {
        this.id = id;
        this.isTop = isTop;
    }

    public Article() {
    }

}
