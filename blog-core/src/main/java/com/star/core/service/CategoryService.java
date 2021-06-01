package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.service.dto.CategoryDTO;
import com.star.core.domain.entity.Category;
import com.star.core.domain.vo.ConditionVO;
import com.star.core.service.dto.PageDTO;

import java.util.List;


/**
 * @author zzStar
 */
public interface CategoryService extends IService<Category> {

    /**
     * 查询分类列表
     *
     * @return 分类列表
     */
    PageDTO<CategoryDTO> listCategories();

    /**
     * 查询后台分类
     *
     * @param conditionVO 条件
     * @return 分类列表
     */
    PageDTO<Category> listCategoryBackDTO(ConditionVO conditionVO);

    /**
     * 删除分类
     *
     * @param categoryIdList 分类id集合
     */
    void deleteCategory(List<Integer> categoryIdList);

}
