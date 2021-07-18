package com.star.core.config;

import com.star.core.handler.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.session.HttpSessionEventPublisher;

import javax.annotation.Resource;

/**
 * Security配置
 *
 * @Author: zzStar
 * @Date: 12-18-2020 10:16
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Resource
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    @Resource
    private AccessDeniedHandlerImpl accessDeniedHandler;

    @Resource
    private AuthenticationSuccessHandlerImpl authenticationSuccessHandler;

    @Resource
    private AuthenticationFailHandlerImpl authenticationFailHandler;

    @Resource
    private LogoutSuccessHandlerImpl logoutSuccessHandler;

    @Bean
    public FilterInvocationSecurityMetadataSource securityMetadataSource() {
        return new FilterInvocationSecurityMetadataSourceImpl();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new CustomAccessDecisionManager();
    }

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 配置权限
     *
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // login Or logout
                .formLogin().loginProcessingUrl("/login").successHandler(authenticationSuccessHandler).failureHandler(authenticationFailHandler).and()
                .logout().logoutUrl("/logout").logoutSuccessHandler(logoutSuccessHandler).and()
                // 路由权限
                .authorizeRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
            @Override
            public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                fsi.setSecurityMetadataSource(securityMetadataSource());
                fsi.setAccessDecisionManager(accessDecisionManager());
                return fsi;
            }
        })

                .anyRequest().permitAll()
                .and()
                .headers().frameOptions().disable()
                .and()

                // 关闭跨站请求防护
                .csrf().disable().exceptionHandling()
                // 未登录处理
                .authenticationEntryPoint(authenticationEntryPoint)
                // 权限不足处理
                .accessDeniedHandler(accessDeniedHandler)
                .and()

                .sessionManagement()
                .maximumSessions(20)
                .sessionRegistry(sessionRegistry());
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/admin/**",
                        "/login");
    }

}
