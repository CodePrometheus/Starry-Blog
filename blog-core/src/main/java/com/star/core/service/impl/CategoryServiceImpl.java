package com.star.core.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.core.dto.CategoryBackDTO;
import com.star.core.dto.CategoryDTO;
import com.star.core.dto.CategoryOptionDTO;
import com.star.core.dto.PageData;
import com.star.core.entity.Article;
import com.star.core.entity.Category;
import com.star.core.mapper.ArticleMapper;
import com.star.core.mapper.CategoryMapper;
import com.star.core.service.CategoryService;
import com.star.core.util.BeanCopyUtil;
import com.star.core.util.PageUtils;
import com.star.core.vo.CategoryVO;
import com.star.core.vo.ConditionVO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
    public PageData<CategoryDTO> listCategories() {
        return new PageData<>(categoryMapper.listCategory(), categoryMapper.selectCount(null));
    }

    @Override
    public List<CategoryOptionDTO> listCategoriesBySearch(ConditionVO condition) {
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>().like(
                StringUtils.isNotBlank(condition.getKeywords()), Category::getCategoryName,
                condition.getKeywords()
        ).orderByDesc(Category::getUpdateTime));
        return BeanCopyUtil.copyList(categoryList, CategoryOptionDTO.class);
    }

    @Override
    public PageData<CategoryBackDTO> listCategoryBack(ConditionVO condition) {
        Long categoryCount = categoryMapper.selectCount(new LambdaQueryWrapper<Category>()
                .like(StringUtils.isNotBlank(condition.getKeywords()), Category::getCategoryName,
                        condition.getKeywords()));
        if (categoryCount == 0) {
            return new PageData<>();
        }
        List<CategoryBackDTO> categoryList = categoryMapper.listCategoryBack(PageUtils.getLimitCurrent(), PageUtils.getSize(), condition);
        return new PageData<>(categoryList, categoryCount);
    }

    @Override
    @Transactional(rollbackFor = StarryException.class)
    public void deleteCategory(List<Integer> categoryIdList) {
        // categoryIdList内是有文章in查询
        Long articleCount = articleMapper.selectCount(new LambdaQueryWrapper<Article>()
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
        Category existCategory = categoryMapper.selectOne(new LambdaQueryWrapper<Category>().select(Category::getId)
                .eq(Category::getCategoryName, categoryVO.getCategoryName()));
        if (Objects.nonNull(existCategory) &&
                !existCategory.getId().equals(categoryVO.getId())) {
            throw new StarryException("分类名已存在");
        }
        Category category = Category.builder()
                .id(categoryVO.getId())
                .categoryName(categoryVO.getCategoryName()).build();
        this.saveOrUpdate(category);
    }

}
