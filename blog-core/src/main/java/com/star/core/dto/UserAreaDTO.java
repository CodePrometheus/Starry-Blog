package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zzStar
 * @Date: 08-25-2021 20:02
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAreaDTO {

    /**
     * 地区名 key
     */
    private String name;

    /**
     * 数量 value
     */
    private Long value;

}
