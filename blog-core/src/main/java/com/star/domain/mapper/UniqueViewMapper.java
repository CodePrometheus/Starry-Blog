package com.star.domain.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.star.domain.entity.UniqueView;
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
     * @return 用户量集合
     */
    List<Integer> listUniqueViews();

}
