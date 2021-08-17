package com.star.core.rest;

import com.star.common.constant.PathConst;
import com.star.common.constant.Result;
import com.star.core.service.PageService;
import com.star.core.util.ImageUtil;
import com.star.core.vo.PageVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 08-15-2021 20:45
 */
@RestController
@RequestMapping("admin")
@Api(tags = "页面模块")
public class PageController {

    @Resource
    private ImageUtil imageUtil;

    @Resource
    private PageService pageService;

    @ApiOperation(value = "删除页面")
    @ApiImplicitParam(name = "pageId", value = "页面id", required = true, dataType = "Integer")
    @DeleteMapping("/pages/{pageId}")
    public Result<?> deletePage(@PathVariable("pageId") Integer pageId) {
        pageService.deletePage(pageId);
        return Result.success();
    }

    @ApiOperation(value = "保存或更新页面")
    @PostMapping("/pages")
    public Result<?> saveOrUpdatePage(@Valid @RequestBody PageVO pageVO) {
        pageService.saveOrUpdatePage(pageVO);
        return Result.success();
    }

    @ApiOperation(value = "获取页面列表")
    @GetMapping("/pages")
    public Result<List<PageVO>> listPages() {
        return Result.success(pageService.listPages());
    }

    @ApiOperation(value = "上传页面图片")
    @ApiImplicitParam(name = "file", value = "页面图片", required = true, dataType = "MultipartFile", dataTypeClass = MultipartFile.class)
    @PostMapping("/page/images")
    private Result<String> uploadPageImages(MultipartFile file) {
        return Result.success(imageUtil.upload(file, PathConst.PAGE));
    }

}
