package com.star.core.rest;


import com.star.common.constant.Result;
import com.star.common.constant.StatusConst;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.domain.vo.MessageVO;
import com.star.core.service.MessageService;
import com.star.core.service.dto.MessageBackDTO;
import com.star.core.service.dto.MessageDTO;
import com.star.core.service.dto.PageDTO;
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
    private Result saveMessage(@Valid @RequestBody MessageVO messageVO) {
        messageService.saveMessage(messageVO);
        return new Result(true, StatusConst.OK, "添加成功");
    }

    @ApiOperation(value = "查看留言列表")
    @GetMapping("/messages")
    private Result<List<MessageDTO>> listMessages() {
        return new Result(true, StatusConst.OK, "添加成功", messageService.listMessages());
    }

    @ApiOperation(value = "查看后台留言列表")
    @GetMapping("/admin/messages")
    private Result<PageDTO<MessageBackDTO>> listMessageBackDTO(ConditionVO condition) {
        return new Result(true, StatusConst.OK, "添加成功", messageService.listMessageBackDTO(condition));
    }

    @ApiOperation(value = "删除留言")
    @DeleteMapping("/admin/messages")
    public Result deleteMessages(@RequestBody List<Integer> messageIdList) {
        messageService.removeByIds(messageIdList);
        return new Result(true, StatusConst.OK, "操作成功");
    }

}

