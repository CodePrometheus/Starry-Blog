package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.dto.BlogBackInfoDTO;
import com.star.core.dto.BlogHomeInfoDTO;
import com.star.core.service.BlogInfoService;
import com.star.core.vo.BlogInfoVo;
import com.star.core.vo.WebsiteConfigVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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
    private Result<?> updateAbout(@Valid @RequestBody BlogInfoVo blogInfoVo) {
        blogInfoService.updateAbout(blogInfoVo);
        return Result.success();
    }

    @ApiOperation(value = "获取网站配置")
    @GetMapping("/admin/website/config")
    private Result<WebsiteConfigVO> getWebsiteConfig() {
        return Result.success(blogInfoService.getWebsiteConfig());
    }

    @ApiOperation(value = "更新网站配置")
    @PutMapping("/admin/website/config")
    private Result<WebsiteConfigVO> updateWebsiteConfig(@Valid @RequestBody WebsiteConfigVO websiteConfigVO) {
        blogInfoService.updateWebsiteConfig(websiteConfigVO);
        return Result.success();
    }

    @PostMapping("/report")
    public Result<?> report() {
        blogInfoService.report();
        return Result.success();
    }

}

