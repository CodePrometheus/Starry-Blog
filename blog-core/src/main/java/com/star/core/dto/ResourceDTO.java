package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-15-2021 17:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResourceDTO {

    /**
     * 权限id
     */
    private Integer id;

    /**
     * 资源名
     */
    private String resourceName;

    /**
     * 权限路径
     */
    private String url;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 是否禁用
     */
    private Integer isDisable;

    /**
     * 是否匿名访问
     */
    private Integer isAnonymous;

    /**
     * 权限列表
     */
    private List<ResourceDTO> children;

}
