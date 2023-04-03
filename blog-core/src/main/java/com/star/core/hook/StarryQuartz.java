package com.star.core.hook;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.admin.service.AdminJobLogService;
import com.star.common.tool.IpUtils;
import com.star.common.tool.RedisUtils;
import com.star.inf.dto.UserAreaDTO;
import com.star.inf.entity.UniqueView;
import com.star.inf.entity.UserAuth;
import com.star.inf.mapper.UniqueViewMapper;
import com.star.inf.mapper.UserAuthMapper;
import jakarta.annotation.Resource;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.star.common.constant.CommonConst.UNKNOWN;
import static com.star.common.constant.RedisConst.*;

/**
 * @Author: Starry
 * @Date: 02-19-2023
 */
@Component("starryQuartz")
public class StarryQuartz {

    @Resource
    private RedisUtils redisUtils;

    @Resource
    private UniqueViewMapper uniqueViewMapper;

    @Resource
    private UserAuthMapper userAuthMapper;

    @Resource
    private AdminJobLogService adminJobLogService;

    /**
     * Client: Java => Thrift => Server: Go.MongoNewsHandler()
     */

    /**
     * 统计访问量
     * 向数据库中写入每天的访问量
     */
    public void saveUniqueView() {
        // 获取每天用户量
        Long count = redisUtils.sSize(UNIQUE_VISITOR);
        // 获取昨天日期插入数据
        UniqueView uniqueView = UniqueView.builder()
                .createTime(LocalDateTimeUtil.offset(LocalDateTime.now(ZoneId.of("Asia/Shanghai")), -1, ChronoUnit.DAYS))
                .viewsCount(Optional.of(count.intValue()).orElse(0)).build();
        uniqueViewMapper.insert(uniqueView);
    }


    /**
     * 清空redis访客记录
     */
    public void clear() {
        // 清空redis中的ip
        redisUtils.del(UNIQUE_VISITOR);
        redisUtils.del(VISITOR_AREA);
    }

    /**
     * 统计用户的地域分布
     */
    public void statisticalUserArea() {
        Map<String, Long> userAreaMap = userAuthMapper.selectList(new LambdaQueryWrapper<UserAuth>().select(UserAuth::getIpSource))
                .stream()
                .map(item -> {
                    if (Objects.nonNull(item) && StringUtils.isNotBlank(item.getIpSource())) {
                        return IpUtils.getIpProvince(item.getIpSource());
                    }
                    return UNKNOWN;
                })
                .collect(Collectors.groupingBy(item -> item, Collectors.counting()));
        List<UserAreaDTO> userAreaList = userAreaMap.entrySet().stream()
                .map(item -> UserAreaDTO.builder()
                        .name(item.getKey())
                        .value(item.getValue())
                        .build())
                .collect(Collectors.toList());
        redisUtils.set(USER_AREA, JSON.toJSONString(userAreaList));
    }

    public void clearJobLogs() {
        adminJobLogService.cleanJobLogs();
    }

}
