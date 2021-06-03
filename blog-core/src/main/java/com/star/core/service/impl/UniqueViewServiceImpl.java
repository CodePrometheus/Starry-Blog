package com.star.core.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.core.domain.entity.UniqueView;
import com.star.core.domain.mapper.UniqueViewMapper;
import com.star.core.service.UniqueViewService;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Calendar;

/**
 * @Description: 访问量
 * @Author: zzStar
 * @Date: 12-23-2020 18:43
 */
@Service
public class UniqueViewServiceImpl extends ServiceImpl<UniqueViewMapper, UniqueView> implements UniqueViewService {

    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private UniqueViewMapper uniqueViewMapper;

    @Scheduled(cron = " 0 0 0 * * ?")
    @Override
    public void saveUniqueView() {
        //获取每天用户量
        Long count = redisTemplate.boundSetOps("ip_set").size();
        //获取昨天日期
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, -24);
        uniqueViewMapper.insert(new UniqueView(calendar.getTime(), count.intValue()));
    }

}
