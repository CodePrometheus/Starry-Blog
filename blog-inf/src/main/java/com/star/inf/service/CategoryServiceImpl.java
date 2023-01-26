package com.star.inf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.inf.dto.CategoryDTO;
import com.star.inf.dto.PageData;
import com.star.inf.entity.Category;
import com.star.inf.mapper.CategoryMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

/**
 * 归档业务
 *
 * @Author: zzStar
 * @Date: 12-19-2020 23:07
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements IService<Category> {

    @Resource
    private CategoryMapper categoryMapper;

    public PageData<CategoryDTO> listCategories() {
        return new PageData<>(categoryMapper.listCategory(), categoryMapper.selectCount(null));
    }

}
