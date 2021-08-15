package com.star.core.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.star.core.entity.WebsiteConfig;
import com.star.core.mapper.WebsiteConfigMapper;
import com.star.core.service.WebsiteConfigService;
import org.springframework.stereotype.Service;

/**
 * @Author: zzStar
 * @Date: 08-15-2021 21:13
 */
@Service
public class WebsiteConfigServiceImpl extends ServiceImpl<WebsiteConfigMapper, WebsiteConfig> implements WebsiteConfigService {
}
