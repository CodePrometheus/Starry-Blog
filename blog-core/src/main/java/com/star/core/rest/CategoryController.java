package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.dto.ArticlePreviewListDTO;
import com.star.core.dto.CategoryDTO;
import com.star.core.dto.PageDTO;
import com.star.core.entity.Category;
import com.star.core.service.ArticleService;
import com.star.core.service.CategoryService;
import com.star.core.vo.CategoryVO;
import com.star.core.vo.ConditionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * 归档逻辑
 *
 * @Author: zzStar
 * @Date: 12-19-2020 23:06
 */
@Api(tags = "分类模块")
@RestController
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    @Resource
    private ArticleService articleService;

    @ApiOperation(value = "查看分类列表")
    @GetMapping("/categories")
    private Result<PageDTO<CategoryDTO>> listCategories() {
        return Result.success(categoryService.listCategories());
    }

    @ApiOperation(value = "查看后台分类列表")
    @GetMapping("/admin/categories")
    private Result<PageDTO<Category>> listCategoryBackDTO(ConditionVO condition) {
        return Result.success(categoryService.listCategoryBackDTO(condition));
    }

    @ApiOperation(value = "添加或修改分类")
    @PostMapping("/admin/categories")
    private Result<?> saveOrUpdateCategory(@Valid @RequestBody CategoryVO categoryVO) {
        categoryService.saveOrUpdateCategory(categoryVO);
        return Result.success();
    }

    @ApiOperation(value = "删除分类")
    @DeleteMapping("/admin/categories")
    private Result<?> deleteCategories(@RequestBody List<Integer> categoryIdList) {
        categoryService.deleteCategory(categoryIdList);
        return Result.success();
    }

    @ApiOperation(value = "查看分类下对应的文章")
    @GetMapping("/categories/{categoryId}")
    private Result<ArticlePreviewListDTO> listArticlesByCategoryId(@PathVariable("categoryId") Integer categoryId, Integer current) {
        ConditionVO conditionVO = ConditionVO.builder()
                .categoryId(categoryId)
                .current(current).build();
        return Result.success(articleService.listArticlesByCondition(conditionVO));
    }

}

