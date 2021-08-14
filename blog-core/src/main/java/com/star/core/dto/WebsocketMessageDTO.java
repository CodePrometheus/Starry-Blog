package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zzStar
 * @Date: 06-01-2021 17:22
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebsocketMessageDTO {

    /**
     * 类型
     */
    private Integer type;

    /**
     * 数据
     */
    private Object data;

}
