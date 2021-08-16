package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.dto.MessageBackDTO;
import com.star.core.dto.MessageDTO;
import com.star.core.dto.PageData;
import com.star.core.service.MessageService;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.MessageVO;
import com.star.core.vo.ReviewVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 留言逻辑
 *
 * @Author: zzStar
 * @Date: 12-20-2020 10:10
 */
@Api(tags = "留言模块")
@RestController
public class MessageController {

    @Resource
    private MessageService messageService;

    @ApiOperation(value = "添加留言")
    @PostMapping("/messages")
    private Result<?> saveMessage(@Valid @RequestBody MessageVO messageVO) {
        messageService.saveMessage(messageVO);
        return Result.success();
    }

    @ApiOperation(value = "查看留言列表")
    @GetMapping("/messages")
    private Result<List<MessageDTO>> listMessages() {
        return Result.success(messageService.listMessages());
    }

    @ApiOperation(value = "查看后台留言列表")
    @GetMapping("/admin/messages")
    private Result<PageData<MessageBackDTO>> listMessageBack(ConditionVO condition) {
        return Result.success(messageService.listMessageBack(condition));
    }

    @ApiOperation(value = "删除留言")
    @DeleteMapping("/admin/messages")
    public Result<?> deleteMessages(@RequestBody List<Integer> messageIdList) {
        messageService.removeByIds(messageIdList);
        return Result.success();
    }

    @ApiOperation(value = "审核留言")
    @PutMapping("/admin/messages/review")
    public Result<?> updateMessagesReview(@Valid @RequestBody ReviewVO reviewVO) {
        messageService.updateMessagesReview(reviewVO);
        return Result.success();
    }

}

