package com.star.inf.rest;

import com.star.common.constant.Result;
import com.star.inf.dto.*;
import com.star.inf.service.ArticleServiceImpl;
import com.star.inf.vo.ConditionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private ArticleServiceImpl articleServiceImpl;

    @ApiOperation(value = "根据条件查询文章")
    @GetMapping("/articles/condition")
    public Result<ArticlePreviewListDTO> listArticlesByCondition(ConditionVO conditionVO) {
        return Result.success(articleServiceImpl.listArticlesByCondition(conditionVO));
    }

    @ApiOperation(value = "查看文章归档")
    @GetMapping("/articles/archives")
    private Result<PageData<ArchiveDTO>> listArchives() {
        return Result.success(articleServiceImpl.listArchives());
    }

    @ApiOperation(value = "查看首页文章")
    @GetMapping("/articles")
    private Result<List<ArticleHomeDTO>> listArticles() {
        return Result.success(articleServiceImpl.listArticles());
    }


    @ApiOperation(value = "根据id查看文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer", dataTypeClass = Integer.class)
    @GetMapping("/articles/{articleId}")
    private Result<ArticleDTO> getArticleById(@PathVariable("articleId") Integer articleId) {
        return Result.success(articleServiceImpl.getArticleById(articleId));
    }

    @ApiOperation(value = "搜索文章")
    @GetMapping("/articles/search")
    private Result<List<ArticleSearchDTO>> listArticlesBySearch(ConditionVO condition) {
        return Result.success(articleServiceImpl.listArticlesBySearch(condition));
    }

    @ApiOperation(value = "点赞文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer", dataTypeClass = Integer.class)
    @PostMapping("/articles/{articleId}/like")
    private Result<?> saveArticleLike(@PathVariable("articleId") Integer articleId) {
        articleServiceImpl.saveArticleLike(articleId);
        return Result.success();
    }

}

