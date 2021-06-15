package com.star.core.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.star.core.domain.entity.UniqueView;
import com.star.core.service.dto.UniqueViewDTO;

import java.util.List;

/**
 * @author zzStar
 */
public interface UniqueViewService extends IService<UniqueView> {

    /**
     * 统计每日用户量
     */
    void saveUniqueView();

    /**
     * 获取7天用户量统计
     *
     * @return 用户量
     */
    List<UniqueViewDTO> listUniqueViews();

}
