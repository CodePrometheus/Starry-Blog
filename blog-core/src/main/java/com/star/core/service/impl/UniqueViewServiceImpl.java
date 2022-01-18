package com.star.core.service.impl;

import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.LocalDateTimeUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.tool.RedisUtil;
import com.star.core.dto.UniqueViewDTO;
import com.star.core.entity.UniqueView;
import com.star.core.mapper.UniqueViewMapper;
import com.star.core.service.UniqueViewService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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
public class UniqueViewServiceImpl extends ServiceImpl<UniqueViewMapper, UniqueView> implements UniqueViewService {

    @Resource
    private RedisUtil redisUtil;
    @Resource
    private UniqueViewMapper uniqueViewMapper;

    @Override
    @Scheduled(cron = " 0 0 0 * * ?")
    public void saveUniqueView() {
        // 获取每天用户量
        Long count = redisUtil.sSize(UNIQUE_VISITOR);
        // 获取昨天日期插入数据
        UniqueView uniqueView = UniqueView.builder()
                .createTime(LocalDateTimeUtil.offset(LocalDateTime.now(ZoneId.of("Asia/Shanghai")), -1, ChronoUnit.DAYS))
                .viewsCount(Optional.of(count.intValue()).orElse(0)).build();
        uniqueViewMapper.insert(uniqueView);
    }

    @Override
    public List<UniqueViewDTO> listUniqueViews() {
        // 一周之内
        DateTime start = DateUtil.beginOfDay(DateUtil.offsetDay(new Date(), -7));
        DateTime end = DateUtil.endOfDay(new Date());
        return uniqueViewMapper.listUniqueViews(start, end);
    }

}
