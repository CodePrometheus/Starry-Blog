package com.star.admin.rest;

import com.star.admin.service.AdminFriendLinkService;
import com.star.common.constant.Result;
import com.star.inf.dto.FriendLinkBackDTO;
import com.star.inf.dto.PageData;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.FriendLinkVO;
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
public class AdminFriendLinkController {

    @Resource
    private AdminFriendLinkService adminFriendLinkService;

    @ApiOperation(value = "查看后台友链列表")
    @GetMapping("/links")
    private Result<PageData<FriendLinkBackDTO>> listFriendLink(ConditionVO condition) {
        return Result.success(adminFriendLinkService.listFriendLink(condition));
    }

    @ApiOperation(value = "保存或修改友链")
    @PostMapping("/links")
    private Result<?> saveOrUpdateFriendLink(@Valid @RequestBody FriendLinkVO friendLinkVO) {
        adminFriendLinkService.saveOrUpdateFriendLink(friendLinkVO);
        return Result.success();
    }

    @ApiOperation(value = "删除友链")
    @DeleteMapping("/links")
    private Result<?> deleteFriendLink(@RequestBody List<Integer> linkIdList) {
        adminFriendLinkService.removeByIds(linkIdList);
        return Result.success();
    }
}
