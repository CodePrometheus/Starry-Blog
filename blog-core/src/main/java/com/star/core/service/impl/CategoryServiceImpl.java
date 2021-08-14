package com.star.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.core.entity.Article;
import com.star.core.entity.Category;
import com.star.core.mapper.ArticleMapper;
import com.star.core.mapper.CategoryMapper;
import com.star.core.vo.CategoryVO;
import com.star.core.vo.ConditionVO;
import com.star.core.service.CategoryService;
import com.star.core.dto.CategoryDTO;
import com.star.core.dto.PageDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * 归档业务
 *
 * @Author: zzStar
 * @Date: 12-19-2020 23:07
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private ArticleMapper articleMapper;

    @Override
    public PageDTO<CategoryDTO> listCategories() {
        return new PageDTO<>(categoryMapper.listCategoryDTO(), categoryMapper.selectCount(null));
    }

    @Override
    public PageDTO<Category> listCategoryBackDTO(ConditionVO condition) {
        Page<Category> page = new Page<>(condition.getCurrent(), condition.getSize());
        Page<Category> categoryPage = categoryMapper.selectPage(page, new LambdaQueryWrapper<Category>()
                .select(Category::getId, Category::getCategoryName, Category::getCreateTime)
                .like(StringUtils.isNotBlank(condition.getKeywords()),
                        Category::getCategoryName, condition.getKeywords())
                .orderByDesc(Category::getId));
        return new PageDTO<>(categoryPage.getRecords(), (int) categoryPage.getTotal());
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void deleteCategory(List<Integer> categoryIdList) {
        // categoryIdList内是有文章in查询
        Integer articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
                .in(Article::getCategoryId, categoryIdList));
        if (articleCount > 0) {
            throw new StarryException("删除失败，该分类下存在文章");
        }
        categoryMapper.deleteBatchIds(categoryIdList);
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void saveOrUpdateCategory(CategoryVO categoryVO) {
        // 判断是否重复
        Integer categoryCount = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .eq(Category::getCategoryName, categoryVO.getCategoryName()));
        if (categoryCount > 0) {
            throw new StarryException("分类名已存在");
        }
        Category category = Category.builder()
                .id(categoryVO.getId())
                .categoryName(categoryVO.getCategoryName())
                .createTime(Objects.isNull(categoryVO.getId()) ? new Date() : null).build();
        this.saveOrUpdate(category);
    }

}
