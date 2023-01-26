package com.star.inf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * @Author: zzStar
 * @Date: 08-16-2021 21:09
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TagBackDTO {

    /**
     * 标签id
     */
    private Integer id;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 文章量
     */
    private Integer articleCount;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

}
