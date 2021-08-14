package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.dto.BlogBackInfoDTO;
import com.star.core.dto.BlogHomeInfoDTO;
import com.star.core.service.BlogInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 博客信息模块
 * about me + notice
 *
 * @Author: zzStar
 * @Date: 12-19-2020 19:36
 */
@Api(tags = "博客信息模块")
@RestController
public class BlogInfoController {

    @Resource
    private BlogInfoService blogInfoService;

    @ApiOperation(value = "查看博客信息")
    @GetMapping("/")
    private Result<BlogHomeInfoDTO> getBlogHomeInfo() {
        return Result.success(blogInfoService.getBlogInfo());
    }

    @ApiOperation(value = "查看后台信息")
    @GetMapping("/admin")
    private Result<BlogBackInfoDTO> getBlogBackInfo() {
        return Result.success(blogInfoService.getBlogBackInfo());
    }

    @ApiOperation(value = "查看关于我信息")
    @GetMapping("/about")
    private Result<String> getAbout() {
        return Result.success(blogInfoService.getAbout());
    }

    @ApiOperation(value = "修改关于我信息")
    @PutMapping("/admin/about")
    private Result<?> updateAbout(String aboutContent) {
        blogInfoService.updateAbout(aboutContent);
        return Result.success();
    }

    @ApiOperation(value = "修改公告")
    @PutMapping("/admin/notice")
    private Result<?> updateNotice(String notice) {
        blogInfoService.updateNotice(notice);
        return Result.success();
    }

    @ApiOperation(value = "查看公告")
    @GetMapping("/admin/notice")
    private Result<String> getNotice() {
        return Result.success(blogInfoService.getNotice());
    }

}

