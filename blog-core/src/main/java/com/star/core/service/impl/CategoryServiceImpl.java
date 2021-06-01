package com.star.core.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.core.domain.entity.Article;
import com.star.core.domain.entity.Category;
import com.star.core.domain.mapper.ArticleMapper;
import com.star.core.domain.mapper.CategoryMapper;
import com.star.core.domain.vo.ConditionVO;
import com.star.common.exception.StarryException;
import com.star.core.service.CategoryService;
import com.star.core.service.dto.CategoryDTO;
import com.star.core.service.dto.PageDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 归档业务
 *
 * @Author: zzStar
 * @Date: 12-19-2020 23:07
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public PageDTO<CategoryDTO> listCategories() {
        //获取分类列表
        List<CategoryDTO> categoryDTOList = categoryMapper.listCategoryDTO();
        //获取分类数量
        Integer count = categoryMapper.selectCount(null);
        return new PageDTO(categoryDTOList, count);
    }

    @Override
    public PageDTO<Category> listCategoryBackDTO(ConditionVO condition) {
        Page<Category> page = new Page<>(condition.getCurrent(), condition.getSize());
        QueryWrapper<Category> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id", "category_name", "create_time")
                .like(condition.getKeywords() != null, "category_name", condition.getKeywords());
        Page<Category> categoryPage = categoryMapper.selectPage(page, queryWrapper);
        return new PageDTO(categoryPage.getRecords(), (int) categoryPage.getTotal());
    }

    @Transactional(rollbackFor = StarryException.class)
    @Override
    public void deleteCategory(List<Integer> categoryIdList) {
        //查询分类id下是否有文章
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.select("id").in("category_id", categoryIdList);
        if (!articleMapper.selectList(queryWrapper).isEmpty()) {
            throw new StarryException("删除失败，该分类下存在文章");
        }
        categoryMapper.deleteBatchIds(categoryIdList);
    }

}
