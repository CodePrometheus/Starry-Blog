package com.star.core.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 评论VO
 *
 * @Author: zzStar
 * @Date: 12-18-2020 17:29
 */
@Data
@ApiModel(description = "评论")
public class CommentVO {

    /**
     * 回复用户id
     */
    @ApiModelProperty(name = "replyId", value = "回复用户id", dataType = "Integer")
    private Integer replyId;

    /**
     * 评论文章id
     */
    @ApiModelProperty(name = "articleId", value = "文章id", dataType = "Integer")
    private Integer articleId;

    /**
     * 评论内容
     */
    @NotBlank(message = "评论内容不能为空")
    @ApiModelProperty(name = "commentContent", value = "评论内容", required = true, dataType = "String")
    private String commentContent;

    /**
     * 父评论id
     */
    @ApiModelProperty(name = "parentId", value = "评论父id", dataType = "Integer")
    private Integer parentId;

}
