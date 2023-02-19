package com.star.core.config;

import com.star.core.handler.CustomAccessDecisionManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.session.HttpSessionEventPublisher;


/**
 * filterChain
 *
 * @Author: zzStar
 * @Date: 12-18-2020 10:16
 */
@Configuration
@EnableWebSecurity
public class SecurityFilterConfig {

    private static final Logger log = LoggerFactory.getLogger(SecurityFilterConfig.class);

    public static final String LOGIN = "/login";
    public static final String LOGOUT = "/logout";

    @Bean
    public FilterInvocationSecurityMetadataSource securityMetadataSource() {
        return new FilterInvocationSecurityMetadataSourceImpl();
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {
        return new CustomAccessDecisionManager();
    }

    /**
     * 当用户首次访问时，会自动为该用户生成一个sessionId，然后用Cookie作为载体进行记录
     * 用户在会话周期中间访问都会带上Cookie中的内容，因此系统就可以识别出是哪一个用户
     *
     * @return SessionRegistryImpl()
     */
    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    /**
     * 解决session失效后 sessionRegistry 中session没有同步失效的问题，启用并发session控制，首先需要在配置中增加下面监听器
     * 防用户重复登录
     */
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
    @Bean
    public SecurityFilterChain filterChain(
            HttpSecurity http,
            AuthenticationFilterChainHandler handler
    ) {
        try {
            // 允许跨域请求的OPTIONS请求
            http.authorizeHttpRequests().requestMatchers(HttpMethod.OPTIONS).permitAll();

            // login
            http.formLogin().loginProcessingUrl(LOGIN)
                    .successHandler(handler)
                    .failureHandler(handler);

            // 请求校验
            http.authorizeHttpRequests().withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                        @Override
                        public <O extends FilterSecurityInterceptor> O postProcess(O fsi) {
                            fsi.setSecurityMetadataSource(securityMetadataSource());
                            fsi.setAccessDecisionManager(accessDecisionManager());
                            return fsi;
                        }
                    })
                    .anyRequest().permitAll()

                    // 关闭跨站请求防护
                    // .and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                    // .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
                    .and().csrf().disable()

                    // 防止 iframe 内容无法显示
                    .headers().frameOptions().disable().and()

                    // 自定义权限拒绝处理类
                    .exceptionHandling().authenticationEntryPoint(handler)
                    .accessDeniedHandler(handler)

                    // 不会创建 HttpSession
                    .and().sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                    // JWT
                    .and().addFilterBefore(handler, UsernamePasswordAuthenticationFilter.class);
            return http.build();
        } catch (Exception e) {
            log.error("filterChain failed: ", e);
        }
        return null;
    }

}
