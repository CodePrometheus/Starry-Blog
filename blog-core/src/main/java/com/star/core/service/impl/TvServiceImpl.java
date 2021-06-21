package com.star.core.service.impl;

import com.star.core.domain.entity.Tv;
import com.star.core.domain.mapper.TvMapper;
import com.star.core.domain.vo.TvVO;
import com.star.core.service.TvService;
import com.star.core.service.dto.PageDTO;
import com.star.core.util.BeanCopyUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 04-10-2021 16:29
 */
@Service
public class TvServiceImpl implements TvService {

    @Resource
    private TvMapper tvMapper;

    @Override
    public PageDTO<TvVO> list() {
        List<Tv> tvList = tvMapper.selectList(null);
        List<TvVO> list = BeanCopyUtil.copyList(tvList, TvVO.class);
        PageDTO<TvVO> pageDTO = new PageDTO<>();
        pageDTO.setRecordList(list);
        return pageDTO;
    }

}
