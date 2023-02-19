package com.star.core.config;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.star.common.tool.BeanCopyUtil;
import com.star.core.hook.LogPointer;
import com.star.inf.dto.ResourceRoleDTO;
import com.star.inf.entity.BlogResource;
import com.star.inf.entity.UserInfo;
import com.star.inf.mapper.ResourceMapper;
import com.star.inf.mapper.RoleMapper;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

/**
 * 储存请求与权限的对应关系
 *
 * @Author: zzStar
 * @Date: 06-16-2021 13:54
 */
@Component
public class FilterInvocationSecurityMetadataSourceImpl implements FilterInvocationSecurityMetadataSource {

    private static final String ADMIN = "admin";

    @Resource
    private ResourceMapper resourceMapper;

    @Resource
    private LogPointer logPointer;

    @Resource
    private RoleMapper roleMapper;

    /**
     * 接口角色列表
     */
    private static List<ResourceRoleDTO> resourceRoleList;

    /**
     * 加载接口角色信息
     */
    @PostConstruct
    private void loadDataSource() {
        resourceRoleList = roleMapper.listUrlRoles();
    }

    /**
     * 清空接口角色信息
     */
    public void clearDataSource() {
        resourceRoleList = null;
    }

    /**
     * 当接收到一个http请求时, filterSecurityInterceptor会调用的方法.
     * 参数object是一个包含url信息的HttpServletRequest实例. 这个方法要返回请求该url所需要的所有权限集合
     *
     * @param object 用于获取当前请求的url
     * @return 当前请求url所需的角色
     * @throws IllegalArgumentException
     */
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        // 修改接口角色关系后重新加载
        if (CollectionUtils.isEmpty(resourceRoleList)) {
            this.loadDataSource();
        }
        FilterInvocation fi = (FilterInvocation) object;
        // 获取请求方式
        String method = fi.getRequest().getMethod();
        // 获取请求url
        String url = fi.getRequest().getRequestURI();
        // 路径匹配符 直接用以匹配路径
        AntPathMatcher matcher = new AntPathMatcher();

        // Log
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserInfo userInfo = BeanCopyUtil.copyObject(authentication.getPrincipal(), UserInfo.class);
        BlogResource blogResource;
        blogResource = resourceMapper.selectOne(new LambdaQueryWrapper<>(BlogResource.builder()
                .url(url).requestMethod(method).build()));
        logPointer.doPoint(fi.getRequest(), fi.getResponse(), userInfo, blogResource,
                fi.getRequest().getRequestURI().contains(ADMIN));

        // 获取接口角色信息，若为匿名接口则放行，若无对应角色则禁止
        for (ResourceRoleDTO urlRoleDTO : resourceRoleList) {
            if (matcher.match(urlRoleDTO.getUrl(), url)
                    && urlRoleDTO.getRequestMethod().equals(method)) {
                List<String> roleList = urlRoleDTO.getRoleList();
                if (CollectionUtils.isEmpty(roleList)) {
                    return SecurityConfig.createList("disable");
                }
                return SecurityConfig.createList(roleList.toArray(new String[]{}));
            }
        }
        return null;
    }

    /**
     * Spring容器启动时自动调用, 一般把所有请求与权限的对应关系也要在这个方法里初始化, 保存在一个属性变量里
     *
     * @return
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }

    /**
     * 指示该类是否能够为指定的方法调用或Web请求提供ConfigAttributes
     *
     * @param clazz
     * @return
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }

}
