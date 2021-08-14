package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: zzStar
 * @Date: 06-16-2021 14:04
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UrlRoleDTO {

    /**
     * 资源id
     */
    private Integer id;

    /**
     * 路径
     */
    private String url;

    /**
     * 请求方式
     */
    private String requestMethod;

    /**
     * 角色名
     */
    private List<String> roleList;

    /**
     * 是否匿名
     */
    private Integer isAnonymous;

}
