package com.star.admin.rest;

import com.star.admin.service.AdminArticleService;
import com.star.common.constant.PathConst;
import com.star.common.constant.Result;
import com.star.common.tool.ImageUtil;
import com.star.inf.dto.ArticleBackDTO;
import com.star.inf.dto.PageData;
import com.star.inf.vo.ArticleVO;
import com.star.inf.vo.ConditionVO;
import com.star.inf.vo.DeleteVO;
import com.star.inf.vo.TopVO;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@RestController
@RequestMapping("admin")
public class AdminArticleController {

    @Resource
    private AdminArticleService adminArticleService;

    @Resource
    private ImageUtil imageUtil;

    @ApiOperation(value = "查看后台文章")
    @GetMapping("/articles")
    public Result<PageData<ArticleBackDTO>> listArticleBack(ConditionVO conditionVO) {
        return Result.success(adminArticleService.listArticleBack(conditionVO));
    }

    @ApiOperation(value = "添加或修改文章")
    @PostMapping("/articles")
    public Result<?> saveArticle(@Valid @RequestBody ArticleVO articleVO) {
        adminArticleService.saveOrUpdateArticle(articleVO);
        return Result.success();
    }

    @ApiOperation(value = "修改文章置顶状态")
    @PutMapping("/articles/top")
    public Result<?> updateArticleTop(@Valid @RequestBody TopVO articleTopVO) {
        adminArticleService.updateArticleTop(articleTopVO);
        return Result.success();
    }

    @ApiOperation(value = "上传文章图片")
    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile", dataTypeClass = MultipartFile.class)
    @PostMapping("/articles/images")
    public Result<String> saveArticleImages(MultipartFile file) {
        return Result.success(imageUtil.upload(file, PathConst.ARTICLE));
    }

    @ApiOperation(value = "恢复或删除文章")
    @PutMapping("/articles")
    public Result<?> updateArticleDelete(@Valid @RequestBody DeleteVO deleteVO) {
        adminArticleService.updateArticleDelete(deleteVO);
        return Result.success();
    }

    @ApiOperation(value = "物理删除文章")
    @DeleteMapping("/articles")
    public Result<?> deleteArticles(@RequestBody List<Integer> articleIdList) {
        adminArticleService.deleteArticles(articleIdList);
        return Result.success();
    }

    @ApiOperation(value = "根据id查看后台文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer", dataTypeClass = Integer.class)
    @GetMapping("/articles/{articleId}")
    public Result<ArticleVO> getArticleBackById(@PathVariable("articleId") Integer articleId) {
        return Result.success(adminArticleService.getArticleBackById(articleId));
    }

}
