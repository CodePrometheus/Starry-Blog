package com.star.inf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.inf.dto.UniqueViewDTO;
import com.star.inf.entity.UniqueView;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
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
     * @param start 开始时间
     * @param end   结束时间
     * @return 用户量集合
     */
    List<UniqueViewDTO> listUniqueViews(@Param("start") Date start, @Param("end") Date end);

}
