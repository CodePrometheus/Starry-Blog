package com.star.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.star.core.domain.vo.CommentVO;
import com.star.core.util.UserUtil;
import lombok.Data;

import java.util.Date;

/**
 * 评论
 *
 * @Author: zzStar
 * @Date: 12-18-2020 17:26
 */
@Data
@TableName("tb_comment")
public class Comment {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 评论用户Id
     */
    private Integer userId;

    /**
     * 回复用户id
     */
    private Integer replyId;

    /**
     * 评论文章id
     */
    private Integer articleId;

    /**
     * 评论内容
     */
    private String commentContent;

    /**
     * 评论时间
     */
    private Date createTime;

    /**
     * 父评论id
     */
    private Integer parentId;

    /**
     * 状态码
     */
    private Integer isDelete;

    public Comment(CommentVO commentVO) {
        this.userId = UserUtil.getLoginUser().getUserInfoId();
        this.replyId = commentVO.getReplyId();
        this.articleId = commentVO.getArticleId();
        this.commentContent = commentVO.getCommentContent();
        this.parentId = commentVO.getParentId();
        this.createTime = new Date();
    }

    public Comment(Integer commentId, Integer isDelete) {
        this.id = commentId;
        this.isDelete = isDelete;
    }

}
