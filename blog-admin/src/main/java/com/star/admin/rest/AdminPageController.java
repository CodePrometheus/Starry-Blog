package com.star.admin.rest;

import com.star.admin.service.AdminPageService;
import com.star.common.constant.PathConst;
import com.star.common.constant.Result;
import com.star.common.tool.ImageUtil;
import com.star.inf.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 08-15-2021 20:45
 */
@Api(tags = "页面模块")
@RestController
@RequestMapping("admin")
public class AdminPageController {

    @Resource
    private ImageUtil imageUtil;

    @Resource
    private AdminPageService adminPageService;

    @ApiOperation(value = "删除页面")
    @ApiImplicitParam(name = "pageId", value = "页面id", required = true, dataType = "Integer")
    @DeleteMapping("/pages/{pageId}")
    public Result<?> deletePage(@PathVariable("pageId") Integer pageId) {
        adminPageService.deletePage(pageId);
        return Result.success();
    }

    @ApiOperation(value = "保存或更新页面")
    @PostMapping("/pages")
    public Result<?> saveOrUpdatePage(@Valid @RequestBody PageVO pageVO) {
        adminPageService.saveOrUpdatePage(pageVO);
        return Result.success();
    }

    @ApiOperation(value = "获取页面列表")
    @GetMapping("/pages")
    public Result<List<PageVO>> listPages() {
        return Result.success(adminPageService.listPages());
    }

    @ApiOperation(value = "上传页面图片")
    @ApiImplicitParam(name = "file", value = "页面图片", required = true, dataType = "MultipartFile", dataTypeClass = MultipartFile.class)
    @PostMapping("/page/images")
    public Result<String> uploadPageImages(MultipartFile file) {
        return Result.success(imageUtil.upload(file, PathConst.PAGE));
    }

}
