package com.star.core.config;

import com.star.admin.service.AdminOperationLogService;
import com.star.core.hook.DefaultLogPointerHooks;
import com.star.core.hook.LogPointer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Bean 实例配置
 *
 * @Author: zzStar
 * @Date: 01-03-2022 22:43
 */
@Configuration
public class BeanConfig {

    public BeanConfig() {
    }

    @Bean
    @ConditionalOnMissingBean(LogPointer.class)
    public LogPointer logPointer(AdminOperationLogService operationLogService) {
        return new DefaultLogPointerHooks(operationLogService);
    }

}
