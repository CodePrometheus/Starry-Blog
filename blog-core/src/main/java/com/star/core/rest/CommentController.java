package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.domain.vo.CommentVO;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.domain.vo.DeleteVO;
import com.star.core.service.CommentService;
import com.star.core.service.dto.CommentBackDTO;
import com.star.core.service.dto.CommentDTO;
import com.star.core.service.dto.PageDTO;
import com.star.core.service.dto.ReplyDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.star.common.constant.MessageConst.*;
import static com.star.common.constant.StatusConst.OK;

/**
 * 评论逻辑
 *
 * @Author: zzStar
 * @Date: 12-20-2020 15:02
 */
@RestController
@Api(tags = "评论模块")
public class CommentController {

    @Resource
    private CommentService commentService;

    @ApiOperation(value = "查询评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "current", value = "当前页码", required = true, dataType = "Long", dataTypeClass = Long.class)
    })
    @GetMapping("/comments")
    private Result<PageDTO<CommentDTO>> listComments(Integer articleId, Long current) {
        return new Result<>(true, OK, QUERY, commentService.listComments(articleId, current));
    }

    @ApiOperation(value = "添加评论或回复")
    @PostMapping("/comments")
    private Result<?> saveComment(@Valid @RequestBody CommentVO commentVO) {
        commentService.saveComment(commentVO);
        return new Result<>(true, OK, COMMENT);
    }

    @ApiOperation(value = "查询评论下的回复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commentId", value = "文章id", required = true, dataType = "Integer", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "current", value = "当前页码", required = true, dataType = "Long", dataTypeClass = Long.class)
    })
    @GetMapping("/comments/replies")
    private Result<List<ReplyDTO>> listRepliesByCommentId(Integer commentId, Long current) {
        return new Result<>(true, OK, QUERY, commentService.listRepliesByCommentId(commentId, current));
    }

    @ApiOperation(value = "评论点赞")
    @PostMapping("/comments/like")
    private Result<?> saveCommentList(Integer commentId) {
        commentService.saveCommentLike(commentId);
        return new Result<>(true, OK, LIKE);
    }

    @ApiOperation(value = "删除或恢复评论")
    @PutMapping("/admin/comments")
    private Result<?> deleteComment(DeleteVO deleteVO) {
        commentService.updateCommentDelete(deleteVO);
        return new Result<>(true, OK, OPERATE);
    }

    @ApiOperation(value = "物理删除评论")
    @DeleteMapping("/admin/comments")
    public Result<?> deleteComments(@RequestBody List<Integer> commentIdList) {
        commentService.removeByIds(commentIdList);
        return new Result<>(true, OK, OPERATE);
    }

    @ApiOperation(value = "查询后台评论")
    @GetMapping("/admin/comments")
    private Result<PageDTO<CommentBackDTO>> listCommentBackDTO(ConditionVO condition) {
        return new Result<>(true, OK, QUERY, commentService.listCommentBackDTO(condition));
    }

}

