package com.star.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.star.domain.entity.UniqueView;

/**
 * @author zzStar
 */
public interface UniqueViewService extends IService<UniqueView> {

    /**
     * 统计每日用户量
     */
    void saveUniqueView();

}
