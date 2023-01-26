package com.star.inf.rest;

import com.star.common.constant.Result;
import com.star.inf.dto.PageData;
import com.star.inf.dto.TagDTO;
import com.star.inf.service.TagServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 标签逻辑
 *
 * @Author: zzStar
 * @Date: 12-20-2020 11:02
 */
@Api(tags = "标签模块")
@RestController
public class TagController {

    @Resource
    private TagServiceImpl tagService;

    @ApiOperation(value = "查看标签列表")
    @GetMapping("/tags")
    private Result<PageData<TagDTO>> listTags() {
        return Result.success(tagService.listTags());
    }

}

