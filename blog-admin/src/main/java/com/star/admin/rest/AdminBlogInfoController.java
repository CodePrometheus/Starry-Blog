package com.star.admin.rest;

import com.star.admin.service.AdminBlogInfoService;
import com.star.common.constant.Result;
import com.star.inf.dto.BlogBackInfoDTO;
import com.star.inf.vo.BlogInfoVo;
import com.star.inf.vo.WebsiteConfigVO;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@RestController
@RequestMapping("admin")
public class AdminBlogInfoController {

    @Resource
    private AdminBlogInfoService adminBlogInfoService;

    @ApiOperation(value = "查看后台信息")
    @GetMapping("/")
    public Result<BlogBackInfoDTO> getBlogBackInfo() {
        return Result.success(adminBlogInfoService.getBlogBackInfo());
    }

    @ApiOperation(value = "修改关于我信息")
    @PutMapping("/about")
    public Result<?> updateAbout(@Valid @RequestBody BlogInfoVo blogInfoVo) {
        adminBlogInfoService.updateAbout(blogInfoVo);
        return Result.success();
    }

    @ApiOperation(value = "更新网站配置")
    @PutMapping("/website/config")
    public Result<WebsiteConfigVO> updateWebsiteConfig(@Valid @RequestBody WebsiteConfigVO websiteConfigVO) {
        adminBlogInfoService.updateWebsiteConfig(websiteConfigVO);
        return Result.success();
    }

}
