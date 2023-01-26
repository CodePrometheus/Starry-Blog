package com.star.admin.rest;

import com.star.admin.service.AdminCategoryService;
import com.star.common.constant.Result;
import com.star.inf.dto.CategoryBackDTO;
import com.star.inf.dto.CategoryOptionDTO;
import com.star.inf.dto.PageData;
import com.star.inf.vo.CategoryVO;
import com.star.inf.vo.ConditionVO;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@RestController
@RequestMapping("admin")
public class AdminCategoryController {

    @Resource
    private AdminCategoryService adminCategoryService;

    @ApiOperation(value = "查看后台分类列表")
    @GetMapping("/categories")
    private Result<PageData<CategoryBackDTO>> listCategoryBack(ConditionVO condition) {
        return Result.success(adminCategoryService.listCategoryBack(condition));
    }

    @ApiOperation(value = "添加或修改分类")
    @PostMapping("/categories")
    private Result<?> saveOrUpdateCategory(@Valid @RequestBody CategoryVO categoryVO) {
        adminCategoryService.saveOrUpdateCategory(categoryVO);
        return Result.success();
    }

    @ApiOperation(value = "删除分类")
    @DeleteMapping("/categories")
    private Result<?> deleteCategories(@RequestBody List<Integer> categoryIdList) {
        adminCategoryService.deleteCategory(categoryIdList);
        return Result.success();
    }

    @ApiOperation(value = "搜索文章分类")
    @GetMapping("/categories/search")
    public Result<List<CategoryOptionDTO>> listCategoriesBySearch(ConditionVO condition) {
        return Result.success(adminCategoryService.listCategoriesBySearch(condition));
    }

}
