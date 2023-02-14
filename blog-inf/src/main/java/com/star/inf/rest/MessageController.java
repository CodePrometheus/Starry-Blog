package com.star.inf.rest;

import com.star.common.constant.Result;
import com.star.inf.dto.MessageDTO;
import com.star.inf.service.MessageServiceImpl;
import com.star.inf.vo.MessageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
    private MessageServiceImpl messageService;

    @ApiOperation(value = "添加留言")
    @PostMapping("/messages")
    public Result<?> saveMessage(@Valid @RequestBody MessageVO messageVO) {
        messageService.saveMessage(messageVO);
        return Result.success();
    }

    @ApiOperation(value = "查看留言列表")
    @GetMapping("/messages")
    public Result<List<MessageDTO>> listMessages() {
        return Result.success(messageService.listMessages());
    }

}

