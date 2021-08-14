package com.star.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.core.entity.Category;
import com.star.core.dto.CategoryDTO;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Description: 归档
 * @Author: zzStar
 * @Date: 12-19-2020 14:09
 */
@Repository
public interface CategoryMapper extends BaseMapper<Category> {

    /**
     * 查询归档和对应文章数量
     *
     * @return 归档
     */
    List<CategoryDTO> listCategoryDTO();

}
