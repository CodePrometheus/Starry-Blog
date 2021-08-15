package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.dto.BlogBackInfoDTO;
import com.star.core.dto.BlogHomeInfoDTO;
import com.star.core.service.BlogInfoService;
import com.star.core.vo.BlogInfoVo;
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
    private Result<?> updateAbout(BlogInfoVo blogInfoVo) {
        blogInfoService.updateAbout(blogInfoVo);
        return Result.success();
    }

}

