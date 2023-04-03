package com.star.admin.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.StringUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.exception.StarryException;
import com.star.common.tool.BeanCopyUtils;
import com.star.common.tool.PageUtils;
import com.star.inf.dto.CategoryBackDTO;
import com.star.inf.dto.CategoryOptionDTO;
import com.star.inf.dto.PageData;
import com.star.inf.entity.Article;
import com.star.inf.entity.Category;
import com.star.inf.mapper.ArticleMapper;
import com.star.inf.mapper.CategoryMapper;
import com.star.inf.vo.CategoryVO;
import com.star.inf.vo.ConditionVO;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * @Author: Starry
 * @Date: 01-26-2023
 */
@Service
public class AdminCategoryService extends ServiceImpl<CategoryMapper, Category> {

    @Resource
    private ArticleMapper articleMapper;

    @Resource
    private CategoryMapper categoryMapper;

    /**
     * 搜索文章分类
     *
     * @param condition 条件
     * @return {@link List<CategoryOptionDTO>} 分类列表
     */
    public List<CategoryOptionDTO> listCategoriesBySearch(ConditionVO condition) {
        List<Category> categoryList = categoryMapper.selectList(new LambdaQueryWrapper<Category>().like(
                StringUtils.isNotBlank(condition.getKeywords()), Category::getCategoryName,
                condition.getKeywords()
        ).orderByDesc(Category::getUpdateTime));
        return BeanCopyUtils.copyList(categoryList, CategoryOptionDTO.class);
    }

    /**
     * 查询后台分类
     *
     * @param condition 条件
     * @return 分类列表
     */
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

    /**
     * 删除分类
     *
     * @param categoryIdList 分类id集合
     */
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

    /**
     * 添加或修改分类
     *
     * @param categoryVO 分类
     */
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
