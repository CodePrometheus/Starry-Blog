package com.star.inf.service;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.tool.RedisUtils;
import com.star.inf.dto.UniqueViewDTO;
import com.star.inf.entity.UniqueView;
import com.star.inf.mapper.UniqueViewMapper;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static com.star.common.constant.RedisConst.UNIQUE_VISITOR;

/**
 * @Description: 访问量
 * @Author: zzStar
 * @Date: 12-23-2020 18:43
 */
@Service
public class UniqueViewServiceImpl extends ServiceImpl<UniqueViewMapper, UniqueView> implements IService<UniqueView> {

    @Resource
    private UniqueViewMapper uniqueViewMapper;

    /**
     * @return
     */
    public List<UniqueViewDTO> listUniqueViews() {
        // 一周之内
        DateTime start = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime end = DateUtil.endOfDay(new Date());
        return uniqueViewMapper.listUniqueViews(start, end);
    }

}
