package com.star.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.core.dto.CategoryBackDTO;
import com.star.core.entity.Category;
import com.star.core.dto.CategoryDTO;
import com.star.core.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
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
    List<CategoryDTO> listCategory();

    /**
     * 查询后台分类列表
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return {@link List<CategoryBackDTO>} 分类列表
     */
    List<CategoryBackDTO> listCategoryBack(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);


}
