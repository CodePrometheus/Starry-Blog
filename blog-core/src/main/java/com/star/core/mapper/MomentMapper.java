package com.star.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.core.dto.MomentDTO;
import com.star.core.entity.Moment;
import com.star.core.vo.ConditionVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 2022/1/12 11:14 PM
 */
@Repository
public interface MomentMapper extends BaseMapper<Moment> {

    /**
     * 查询后台动态
     *
     * @param current   页码
     * @param size      大小
     * @param condition 条件
     * @return 动态集合
     */
    List<MomentDTO> listMomentBack(Long current, Long size, ConditionVO condition);

    /**
     * 根据id查看动态
     *
     * @param momentId 说说id
     * @return {@link MomentDTO} 说说信息
     */
    MomentDTO getMomentById(@Param("momentId") Integer momentId);

    /**
     * 根据id查看后台动态
     *
     * @param momentId 动态id
     * @return {@link MomentDTO} 动态信息
     */
    MomentDTO getBackMomentById(@Param("momentId") Integer momentId);

    /**
     * 查询前台动态
     *
     * @param current 页码
     * @param size    大小
     * @return 动态集合
     */
    List<MomentDTO> listMoments(@Param("current") Long current, @Param("size") Long size);

}
