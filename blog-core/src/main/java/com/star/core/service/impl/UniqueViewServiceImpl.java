package com.star.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.common.tool.DateUtil;
import com.star.common.tool.RedisUtil;
import com.star.core.domain.entity.UniqueView;
import com.star.core.domain.mapper.UniqueViewMapper;
import com.star.core.service.UniqueViewService;
import com.star.core.service.dto.UniqueViewDTO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.star.common.constant.RedisConst.IP_SET;


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
        Long count = redisUtil.sSize(IP_SET);
        // 获取昨天日期插入数据
        UniqueView uniqueView = UniqueView.builder()
                .createTime(DateUtil.getSomeDay(new Date(), -1))
                .viewsCount(Objects.nonNull(count) ? count.intValue() : 0).build();
        uniqueViewMapper.insert(uniqueView);
    }

    @Override
    public List<UniqueViewDTO> listUniqueViews() {
        // 一周之内
        String start = DateUtil.getMinTime(DateUtil.getSomeDay(new Date(), -7));
        String end = DateUtil.getMaxTime(new Date());
        return uniqueViewMapper.listUniqueViews(start, end);
    }

}
