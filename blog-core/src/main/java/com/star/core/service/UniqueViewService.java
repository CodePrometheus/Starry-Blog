package com.star.core.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.domain.entity.UniqueView;

/**
 * @author zzStar
 */
public interface UniqueViewService extends IService<UniqueView> {

    /**
     * 统计每日用户量
     */
    void saveUniqueView();

}
