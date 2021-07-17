package com.star.core.rest;

import com.star.common.constant.Result;
import com.star.core.domain.entity.Category;
import com.star.core.domain.vo.CategoryVO;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.service.ArticleService;
import com.star.core.service.CategoryService;
import com.star.core.service.dto.ArticlePreviewListDTO;
import com.star.core.service.dto.CategoryDTO;
import com.star.core.service.dto.PageDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

import static com.star.common.constant.MessageConst.*;
import static com.star.common.constant.StatusConst.OK;

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
        return new Result<>(true, OK, QUERY, categoryService.listCategories());
    }

    @ApiOperation(value = "查看后台分类列表")
    @GetMapping("/admin/categories")
    private Result<PageDTO<Category>> listCategoryBackDTO(ConditionVO condition) {
        return new Result<>(true, OK, QUERY, categoryService.listCategoryBackDTO(condition));
    }

    @ApiOperation(value = "添加或修改分类")
    @PostMapping("/admin/categories")
    private Result<?> saveOrUpdateCategory(@Valid @RequestBody CategoryVO categoryVO) {
        categoryService.saveOrUpdateCategory(categoryVO);
        return new Result<>(true, OK, OPERATE);
    }

    @ApiOperation(value = "删除分类")
    @DeleteMapping("/admin/categories")
    private Result<?> deleteCategories(@RequestBody List<Integer> categoryIdList) {
        categoryService.deleteCategory(categoryIdList);
        return new Result<>(true, OK, DELETE);
    }

    @ApiOperation(value = "查看分类下对应的文章")
    @GetMapping("/categories/{categoryId}")
    private Result<ArticlePreviewListDTO> listArticlesByCategoryId(@PathVariable("categoryId") Integer categoryId, Integer current) {
        ConditionVO conditionVO = ConditionVO.builder()
                .categoryId(categoryId)
                .current(current).build();
        return new Result<>(true, OK, QUERY, articleService.listArticlesByCondition(conditionVO));
    }

}

