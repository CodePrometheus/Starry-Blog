package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.vo.CategoryVO;
import com.star.core.dto.CategoryDTO;
import com.star.core.entity.Category;
import com.star.core.vo.ConditionVO;
import com.star.core.dto.PageData;

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
    PageData<CategoryDTO> listCategories();

    /**
     * 查询后台分类
     *
     * @param conditionVO 条件
     * @return 分类列表
     */
    PageData<Category> listCategoryBackDTO(ConditionVO conditionVO);

    /**
     * 删除分类
     *
     * @param categoryIdList 分类id集合
     */
    void deleteCategory(List<Integer> categoryIdList);

    /**
     * 添加或修改分类
     * @param categoryVO 分类
     */
    void saveOrUpdateCategory(CategoryVO categoryVO);

}
