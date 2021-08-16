package com.star.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.core.dto.TagBackDTO;
import com.star.core.entity.Tag;
import com.star.core.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 标签
 *
 * @Author: zzStar
 * @Date: 12-19-2020 14:08
 */
@Repository
public interface TagMapper extends BaseMapper<Tag> {

    /**
     * 查询后台标签列表
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return {@link List <TagBackDTO>} 标签列表
     */
    List<TagBackDTO> listTagBack(@Param("current") Long current, @Param("size") Long size, @Param("condition") ConditionVO condition);

}
