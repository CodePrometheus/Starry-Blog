package com.star.core.rest;

import com.star.common.constant.PathConst;
import com.star.common.constant.Result;
import com.star.common.tool.ImageUtil;
import com.star.core.domain.vo.ArticleVO;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.domain.vo.DeleteVO;
import com.star.core.service.ArticleService;
import com.star.core.service.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.star.common.constant.MessageConst.*;
import static com.star.common.constant.StatusConst.OK;

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
    private ArticleService articleService;

    @ApiOperation(value = "查看文章归档")
    @ApiImplicitParam(name = "current", value = "当前页码", required = true, dataType = "Long", dataTypeClass = Long.class)
    @GetMapping("/articles/archives")
    private Result<PageDTO<ArchiveDTO>> listArchives(Long current) {
        return new Result<>(true, OK, QUERY, articleService.listArchives(current));
    }

    @ApiOperation(value = "查看首页文章")
    @ApiImplicitParam(name = "current", value = "当前页码", required = true, dataType = "Long", dataTypeClass = Long.class)
    @GetMapping("/articles")
    private Result<List<ArticleHomeDTO>> listArticles(Long current) {
        return new Result<>(true, OK, QUERY, articleService.listArticles(current));
    }

    @ApiOperation(value = "查看最新文章")
    @GetMapping("/articles/newest")
    public Result<List<ArticleRecommendDTO>> listNewestArticles() {
        return new Result<>(true, OK, QUERY, articleService.listNewestArticles());
    }

    @ApiOperation(value = "查看后台文章")
    @GetMapping("/admin/articles")
    private Result<PageDTO<ArticleBackDTO>> listArticleBackDTO(ConditionVO conditionVO) {
        return new Result<>(true, OK, QUERY, articleService.listArticleBackDTO(conditionVO));
    }

    @ApiOperation(value = "查看文章选项")
    @GetMapping("/admin/articles/options")
    private Result<ArticleOptionDTO> listArticleOptionDTO() {
        return new Result<>(true, OK, QUERY, articleService.listArticleOptionDTO());
    }

    @ApiOperation(value = "添加或修改文章")
    @PostMapping("/admin/articles")
    private Result<?> saveArticle(@Valid @RequestBody ArticleVO articleVO) {
        articleService.saveOrUpdateArticle(articleVO);
        return new Result<>(true, OK, OPERATE);
    }

    @ApiOperation(value = "修改文章置顶")
    @PutMapping("/admin/articles/top/{articleId}")
    private Result<?> updateArticleTop(@PathVariable("articleId") Integer articleId, Integer isTop) {
        articleService.updateArticleTop(articleId, isTop);
        return new Result<>(true, OK, UPDATE);
    }

    @ApiOperation(value = "上传文章图片")
    @ApiImplicitParam(name = "file", value = "文章图片", required = true, dataType = "MultipartFile", dataTypeClass = MultipartFile.class)
    @PostMapping("/admin/articles/images")
    private Result<String> saveArticleImages(MultipartFile file) {
        return new Result<>(true, OK, UPLOAD, ImageUtil.upload(file, PathConst.ARTICLE));
    }

    @ApiOperation(value = "恢复或删除文章")
    @PutMapping("/admin/articles")
    private Result<?> updateArticleDelete(DeleteVO deleteVO) {
        articleService.updateArticleDelete(deleteVO);
        return new Result<>(true, OK, OPERATE);
    }

    @ApiOperation(value = "物理删除文章")
    @DeleteMapping("/admin/articles")
    private Result<?> deleteArticles(@RequestBody List<Integer> articleIdList) {
        articleService.deleteArticles(articleIdList);
        return new Result<>(true, OK, OPERATE);
    }

    @ApiOperation(value = "根据id查看后台文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer", dataTypeClass = Integer.class)
    @GetMapping("/admin/articles/{articleId}")
    private Result<ArticleVO> getArticleBackById(@PathVariable("articleId") Integer articleId) {
        return new Result<>(true, OK, QUERY, articleService.getArticleBackById(articleId));
    }

    @ApiOperation(value = "根据id查看文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer", dataTypeClass = Integer.class)
    @GetMapping("/articles/{articleId}")
    private Result<ArticleDTO> getArticleById(@PathVariable("articleId") Integer articleId) {
        return new Result<>(true, OK, QUERY, articleService.getArticleById(articleId));
    }

    @ApiOperation(value = "搜索文章")
    @GetMapping("/articles/search")
    private Result<List<ArticleSearchDTO>> listArticlesBySearch(ConditionVO condition) {
        return new Result<>(true, OK, QUERY, articleService.listArticlesBySearch(condition));
    }

    @ApiOperation(value = "点赞文章")
    @ApiImplicitParam(name = "articleId", value = "文章id", required = true, dataType = "Integer", dataTypeClass = Integer.class)
    @PostMapping("/articles/like")
    private Result<?> saveArticleLike(Integer articleId) {
        articleService.saveArticleLike(articleId);
        return new Result<>(true, OK, LIKE);
    }

}

