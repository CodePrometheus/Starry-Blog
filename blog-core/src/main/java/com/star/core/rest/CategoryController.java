package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.dto.CategoryBackDTO;
import com.star.core.dto.CategoryDTO;
import com.star.core.dto.CategoryOptionDTO;
import com.star.core.dto.PageData;
import com.star.core.service.CategoryService;
import com.star.core.vo.CategoryVO;
import com.star.core.vo.ConditionVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

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

    @ApiOperation(value = "查看分类列表")
    @GetMapping("/categories")
    private Result<PageData<CategoryDTO>> listCategories() {
        return Result.success(categoryService.listCategories());
    }

    @ApiOperation(value = "查看后台分类列表")
    @GetMapping("/admin/categories")
    private Result<PageData<CategoryBackDTO>> listCategoryBack(ConditionVO condition) {
        return Result.success(categoryService.listCategoryBack(condition));
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

    @ApiOperation(value = "搜索文章分类")
    @GetMapping("/admin/categories/search")
    public Result<List<CategoryOptionDTO>> listCategoriesBySearch(ConditionVO condition) {
        return Result.success(categoryService.listCategoriesBySearch(condition));
    }

}

