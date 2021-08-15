package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.dto.CommentBackDTO;
import com.star.core.dto.CommentDTO;
import com.star.core.dto.PageData;
import com.star.core.dto.ReplyDTO;
import com.star.core.service.CommentService;
import com.star.core.vo.CommentVO;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.DeleteVO;
import com.star.core.vo.ReviewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

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
    })
    @GetMapping("/comments")
    private Result<PageData<CommentDTO>> listComments(Integer articleId) {
        return Result.success(commentService.listComments(articleId));
    }

    @ApiOperation(value = "添加评论或回复")
    @PostMapping("/comments")
    private Result<?> saveComment(@Valid @RequestBody CommentVO commentVO) {
        commentService.saveComment(commentVO);
        return Result.success();
    }

    @ApiOperation(value = "查询评论下的回复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "commentId", value = "文章id", required = true, dataType = "Integer", dataTypeClass = Integer.class),
            @ApiImplicitParam(name = "current", value = "当前页码", required = true, dataType = "Long", dataTypeClass = Long.class)
    })
    @GetMapping("/comments/replies")
    private Result<List<ReplyDTO>> listRepliesByCommentId(Integer commentId, Long current) {
        return Result.success(commentService.listRepliesByCommentId(commentId, current));
    }

    @ApiOperation(value = "评论点赞")
    @PostMapping("/comments/like")
    private Result<?> saveCommentList(Integer commentId) {
        commentService.saveCommentLike(commentId);
        return Result.success();
    }

    @ApiOperation(value = "审核评论")
    @PutMapping("/admin/comments/review")
    private Result<?> updateCommentsReview(@Valid @RequestBody ReviewVO reviewVO) {
        commentService.updateCommentsReview(reviewVO);
        return Result.success();
    }

    @ApiOperation(value = "删除评论")
    @DeleteMapping("/admin/comments")
    public Result<?> deleteComments(@RequestBody List<Integer> commentIdList) {
        commentService.removeByIds(commentIdList);
        return Result.success();
    }

    @ApiOperation(value = "查询后台评论")
    @GetMapping("/admin/comments")
    private Result<PageData<CommentBackDTO>> listCommentBackDTO(ConditionVO condition) {
        return Result.success(commentService.listCommentBackDTO(condition));
    }

}

