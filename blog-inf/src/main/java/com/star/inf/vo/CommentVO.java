package com.star.inf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 评论VO
 *
 * @Author: zzStar
 * @Date: 12-18-2020 17:29
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "评论")
public class CommentVO {

    /**
     * 回复用户id
     */
    @ApiModelProperty(name = "replyUserId", value = "回复用户id", dataType = "Integer")
    private Integer replyUserId;

    /**
     * 评论文章id
     */
    @ApiModelProperty(name = "articleId", value = "文章id", dataType = "Integer")
    private Integer articleId;

    /**
     * 评论动态id
     */
    @ApiModelProperty(name = "momentId", value = "动态id", dataType = "Integer")
    private Integer momentId;

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

    /**
     * 类型
     */
    @NotNull(message = "评论类型不能为空")
    @ApiModelProperty(name = "type", value = "评论类型", dataType = "Integer")
    private Integer type;

}
