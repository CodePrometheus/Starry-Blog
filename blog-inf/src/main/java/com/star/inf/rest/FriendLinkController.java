package com.star.inf.rest;

import com.star.common.constant.Result;
import com.star.inf.dto.FriendLinkDTO;
import com.star.inf.service.FriendLinkServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 友链逻辑
 *
 * @Author: zzStar
 * @Date: 12-19-2020 23:38
 */
@Api(tags = "友链模块")
@RestController
public class FriendLinkController {

    @Resource
    private FriendLinkServiceImpl friendLinkService;

    @ApiOperation(value = "查看友链列表")
    @GetMapping("/links")
    public Result<List<FriendLinkDTO>> listFriendLinks() {
        return Result.success(friendLinkService.listFriendLinks());
    }

}

