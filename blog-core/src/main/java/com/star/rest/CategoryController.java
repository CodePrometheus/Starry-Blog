package com.star.rest;


import com.star.constant.Result;
import com.star.constant.StatusConst;
import com.star.domain.entity.Category;
import com.star.domain.vo.CategoryVO;
import com.star.domain.vo.ConditionVO;
import com.star.service.ArticleService;
import com.star.service.CategoryService;
import com.star.service.dto.ArticlePreviewListDTO;
import com.star.service.dto.CategoryDTO;
import com.star.service.dto.PageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ArticleService articleService;

    @ApiOperation(value = "查看分类列表")
    @GetMapping("/categories")
    private Result<PageDTO<CategoryDTO>> listCategories() {
        return new Result(true, StatusConst.OK, "查询成功", categoryService.listCategories());
    }

    @ApiOperation(value = "查看后台分类列表")
    @GetMapping("/admin/categories")
    private Result<PageDTO<Category>> listCategoryBackDTO(ConditionVO condition) {
        return new Result(true, StatusConst.OK, "查询成功", categoryService.listCategoryBackDTO(condition));
    }

    @ApiOperation(value = "添加或修改分类")
    @PostMapping("/admin/categories")
    private Result saveOrUpdateCategory(@Valid @RequestBody CategoryVO categoryVO) {
        categoryService.saveOrUpdate(new Category(categoryVO));
        return new Result(true, StatusConst.OK, "操作成功");
    }

    @ApiOperation(value = "删除分类")
    @DeleteMapping("/admin/categories")
    private Result deleteCategories(@RequestBody List<Integer> categoryIdList) {
        categoryService.deleteCategory(categoryIdList);
        return new Result(true, StatusConst.OK, "删除成功");
    }

    @ApiOperation(value = "查看分类下对应的文章")
    @GetMapping("/categories/{categoryId}")
    private Result<ArticlePreviewListDTO> listArticlesByCategoryId(@PathVariable("categoryId") Integer categoryId, Long current) {
        return new Result(true, StatusConst.OK, "查询成功", articleService.listArticlesByCondition(new ConditionVO(current, categoryId)));
    }

}

