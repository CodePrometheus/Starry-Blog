package com.star.admin.rest;

import com.star.admin.service.AdminMessageService;
import com.star.common.constant.Result;
import com.star.inf.dto.MessageBackDTO;
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
public class AdminMessageController {

    @Resource
    private AdminMessageService adminMessageService;

    @ApiOperation(value = "查看后台留言列表")
    @GetMapping("/messages")
    private Result<PageData<MessageBackDTO>> listMessageBack(ConditionVO condition) {
        return Result.success(adminMessageService.listMessageBack(condition));
    }

    @ApiOperation(value = "删除留言")
    @DeleteMapping("/messages")
    public Result<?> deleteMessages(@RequestBody List<Integer> messageIdList) {
        adminMessageService.removeByIds(messageIdList);
        return Result.success();
    }

    @ApiOperation(value = "审核留言")
    @PutMapping("/messages/review")
    public Result<?> updateMessagesReview(@Valid @RequestBody ReviewVO reviewVO) {
        adminMessageService.updateMessagesReview(reviewVO);
        return Result.success();
    }

}
