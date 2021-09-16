package com.star.core.rest;

import com.star.common.constant.PathConst;
import com.star.common.constant.Result;
import com.star.core.dto.*;
import com.star.core.service.ArticleService;
import com.star.core.util.ImageUtil;
import com.star.core.vo.ArticleTopVO;
import com.star.core.vo.ArticleVO;
import com.star.core.vo.ConditionVO;
import com.star.core.vo.DeleteVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 文章逻辑
 *
 * @Author: zzStar
 * @Date: 12-19-2020 13:35
 */
@Api(tags = "文章模块")
@RestController
public class ArticleController {

    @Resource
    private ImageUtil imageUtil;

    @Resource
    private ArticleService articleService;

    @ApiOperation(value = "根据条件查询文章")
    @GetMapping("/articles/condition")
    public Result<ArticlePreviewListDTO> listArticlesByCondition(ConditionVO conditionVO) {
        return Result.success(articleService.listArticlesByCondition(conditionVO));
    }

    @ApiOperation(value = "查看文章归档")
    @GetMapping("/articles/archives")
    private Result<PageData<ArchiveDTO>> listArchives() {
        return Result.success(articleService.listArchives());
    }

    @ApiOperation(value = "查看首页文章")
    @GetMapping("/articles")
    private Result<List<ArticleHomeDTO>> listArticles() {
        return Result.success(articleService.listArticles());
    }

    @ApiOperation(value = "查看后台文章")
    @GetMapping("/admin/articles")
    private Result<PageData<ArticleBackDTO>> listArticleBack(ConditionVO conditionVO) {
        return Result.success(articleService.listArticleBack(conditionVO));
    }

    @ApiOperation(value = "添加或修改文章")
    @PostMapping("/admin/articles")
    private Result<?> saveArticle(@Valid @RequestBody ArticleVO articleVO) {
        articleService.saveOrUpdateArticle(articleVO);
        return Result.success();
    }

    @ApiOperation(value = "修改文章置顶状态")
    @PutMapping("/admin/articles/top")
    private Result<?> updateArticleTop(@Valid @RequestBody ArticleTopVO articleTopVO) {
        articleService.updateArticleTop(articleTopVO);
        return Result.success();
    }

    @ApiOperation(value = "上传文章图片")
    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile", dataTypeClass = MultipartFile.class)
    @PostMapping("/admin/articles/images")
    private Result<String> saveArticleImages(MultipartFile file) {
        return Result.success(imageUtil.upload(file, PathConst.ARTICLE));
    }

    @ApiOperation(value = "恢复或删除文章")
    @PutMapping("/admin/articles")
    private Result<?> updateArticleDelete(@Valid @RequestBody DeleteVO deleteVO) {
        articleService.updateArticleDelete(deleteVO);
        return Result.success();
    }

    @ApiOperation(value = "物理删除文章")
    @DeleteMapping("/admin/articles")
    private Result<?> deleteArticles(@RequestBody List<Integer> articleIdList) {
        articleService.deleteArticles(articleIdList);
        return Result.success();
    }

    @ApiOperation(value = "根据id查看后台文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer", dataTypeClass = Integer.class)
    @GetMapping("/admin/articles/{articleId}")
    private Result<ArticleVO> getArticleBackById(@PathVariable("articleId") Integer articleId) {
        return Result.success(articleService.getArticleBackById(articleId));
    }

    @ApiOperation(value = "根据id查看文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer", dataTypeClass = Integer.class)
    @GetMapping("/articles/{articleId}")
    private Result<ArticleDTO> getArticleById(@PathVariable("articleId") Integer articleId) {
        return Result.success(articleService.getArticleById(articleId));
    }

    @ApiOperation(value = "搜索文章")
    @GetMapping("/articles/search")
    private Result<List<ArticleSearchDTO>> listArticlesBySearch(ConditionVO condition) {
        return Result.success(articleService.listArticlesBySearch(condition));
    }

    @ApiOperation(value = "点赞文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer", dataTypeClass = Integer.class)
    @PostMapping("/articles/{articleId}/like")
    private Result<?> saveArticleLike(@PathVariable("articleId") Integer articleId) {
        articleService.saveArticleLike(articleId);
        return Result.success();
    }

}

