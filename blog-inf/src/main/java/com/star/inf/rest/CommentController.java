package com.star.inf.rest;

import com.star.common.constant.Result;
import com.star.inf.dto.CommentDTO;
import com.star.inf.dto.PageData;
import com.star.inf.dto.ReplyDTO;
import com.star.inf.service.CommentServiceImpl;
import com.star.inf.vo.CommentVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    private CommentServiceImpl commentService;

    @ApiOperation(value = "查询评论")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer", dataTypeClass = Integer.class),
    })
    @GetMapping("/comments")
    public Result<PageData<CommentDTO>> listComments(CommentVO commentVO) {
        return Result.success(commentService.listComments(commentVO));
    }

    @ApiOperation(value = "添加评论或回复")
    @PostMapping("/comments")
    public Result<?> saveComment(@Valid @RequestBody CommentVO commentVO) {
        commentService.saveComment(commentVO);
        return Result.success();
    }

    @ApiOperation(value = "查询评论下的回复")
    @ApiImplicitParam(name = "commentId", value = "评论id", required = true, dataType = "Integer")
    @GetMapping("/comments/{commentId}/replies")
    public Result<List<ReplyDTO>> listRepliesByCommentId(@PathVariable("commentId") Integer commentId) {
        return Result.success(commentService.listRepliesByCommentId(commentId));
    }

    @ApiOperation(value = "评论点赞")
    @PostMapping("/comments/{commentId}/like")
    public Result<?> saveCommentList(@PathVariable("commentId") Integer commentId) {
        commentService.saveCommentLike(commentId);
        return Result.success();
    }

}

