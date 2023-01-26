package com.star.inf.rest;

import com.star.common.constant.Result;
import com.star.inf.dto.CategoryDTO;
import com.star.inf.dto.PageData;
import com.star.inf.service.CategoryServiceImpl;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

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
    private CategoryServiceImpl categoryService;

    @ApiOperation(value = "查看分类列表")
    @GetMapping("/categories")
    private Result<PageData<CategoryDTO>> listCategories() {
        return Result.success(categoryService.listCategories());
    }

}

