package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 标签
 *
 * @Author: zzStar
 * @Date: 12-19-2020 13:50
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagDTO {

    /**
     * id
     */
    private Integer id;

    /**
     * 标签名
     */
    private String tagName;

}
