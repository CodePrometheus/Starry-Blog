package com.star.admin.rest;

import com.star.admin.service.AdminCommentService;
import com.star.common.constant.Result;
import com.star.inf.dto.CommentBackDTO;
import com.star.inf.dto.PageData;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.ReviewVO;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@RestController
@RequestMapping("admin")
public class AdminCommentController {

    @Resource
    private AdminCommentService adminCommentService;

    @ApiOperation(value = "审核评论")
    @PutMapping("/comments/review")
    private Result<?> updateCommentsReview(@Valid @RequestBody ReviewVO reviewVO) {
        adminCommentService.updateCommentsReview(reviewVO);
        return Result.success();
    }

    @ApiOperation(value = "删除评论")
    @DeleteMapping("/comments")
    public Result<?> deleteComments(@RequestBody List<Integer> commentIdList) {
        adminCommentService.removeByIds(commentIdList);
        return Result.success();
    }

    @ApiOperation(value = "查询后台评论")
    @GetMapping("/comments")
    private Result<PageData<CommentBackDTO>> listCommentBackDTO(ConditionVO condition) {
        return Result.success(adminCommentService.listCommentBackDTO(condition));
    }

}
