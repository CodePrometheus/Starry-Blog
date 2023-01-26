package com.star.inf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.inf.entity.WebsiteConfig;
import com.star.inf.mapper.WebsiteConfigMapper;
import org.springframework.stereotype.Service;

/**
 * @Author: zzStar
 * @Date: 08-15-2021 21:13
 */
@Service
public class WebsiteConfigServiceImpl extends ServiceImpl<WebsiteConfigMapper, WebsiteConfig> implements IService<WebsiteConfig> {
}
