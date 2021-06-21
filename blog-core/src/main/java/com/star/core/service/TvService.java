package com.star.core.service;

import com.star.core.domain.vo.TvVO;
import com.star.core.service.dto.PageDTO;

/**
 * @Author: zzStar
 * @Date: 04-10-2021 16:27
 */
public interface TvService {

    /**
     * 电视列表
     *
     * @return
     */
    PageDTO<TvVO> list();
}
