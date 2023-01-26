package com.star.inf.rest;

import com.star.common.constant.Result;
import com.star.inf.dto.BlogHomeInfoDTO;
import com.star.inf.service.BlogInfoServiceImpl;
import com.star.inf.vo.WebsiteConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 博客信息模块
 *
 * @Author: zzStar
 * @Date: 12-19-2020 19:36
 */
@Api(tags = "博客信息模块")
@RestController
public class BlogInfoController {

    @Resource
    private BlogInfoServiceImpl blogInfoService;

    @ApiOperation(value = "查看博客信息")
    @GetMapping("/")
    private Result<BlogHomeInfoDTO> getBlogHomeInfo() {
        return Result.success(blogInfoService.getBlogInfo());
    }

    @ApiOperation(value = "查看关于我信息")
    @GetMapping("/about")
    private Result<String> getAbout() {
        return Result.success(blogInfoService.getAbout());
    }

    @PostMapping("/report")
    public Result<?> report() {
        blogInfoService.report();
        return Result.success();
    }

    @ApiOperation(value = "获取网站配置")
    @GetMapping("/admin/website/config")
    private Result<WebsiteConfigVO> getWebsiteConfig() {
        return Result.success(blogInfoService.getWebsiteConfig());
    }

}

