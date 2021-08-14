package com.star.core.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.core.entity.UniqueView;
import com.star.core.dto.UniqueViewDTO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * 访问量
 *
 * @Author: zzStar
 * @Date: 12-19-2020 22:03
 */
@Repository
public interface UniqueViewMapper extends BaseMapper<UniqueView> {

    /**
     * 获取一周用户量
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 用户量集合
     */
    List<UniqueViewDTO> listUniqueViews(@Param("startTime") String startTime, @Param("endTime") String endTime);

}
