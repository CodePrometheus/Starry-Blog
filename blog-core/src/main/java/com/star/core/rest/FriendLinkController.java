package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.domain.vo.FriendLinkVO;
import com.star.core.service.FriendLinkService;
import com.star.core.service.dto.FriendLinkBackDTO;
import com.star.core.service.dto.FriendLinkDTO;
import com.star.core.service.dto.PageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.star.common.constant.MessageConst.*;
import static com.star.common.constant.StatusConst.OK;


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
    private FriendLinkService friendLinkService;

    @ApiOperation(value = "查看友链列表")
    @GetMapping("/links")
    private Result<List<FriendLinkDTO>> listFriendLinks() {
        return new Result<>(true, OK, QUERY, friendLinkService.listFriendLinks());
    }

    @ApiOperation(value = "查看后台友链列表")
    @GetMapping("/admin/links")
    private Result<PageDTO<FriendLinkBackDTO>> listFriendLinkDTO(ConditionVO condition) {
        return new Result<>(true, OK, QUERY, friendLinkService.listFriendLinkDTO(condition));
    }

    @ApiOperation(value = "保存或修改友链")
    @PostMapping("/admin/links")
    private Result saveOrUpdateFriendLink(@Valid @RequestBody FriendLinkVO friendLinkVO) {
        friendLinkService.saveOrUpdateFriendLink(friendLinkVO);
        return new Result<>(true, OK, OPERATE);
    }

    @ApiOperation(value = "删除友链")
    @DeleteMapping("/admin/links")
    private Result deleteFriendLink(@RequestBody List<Integer> linkIdList) {
        friendLinkService.removeByIds(linkIdList);
        return new Result<>(true, OK, DELETE);
    }

}

