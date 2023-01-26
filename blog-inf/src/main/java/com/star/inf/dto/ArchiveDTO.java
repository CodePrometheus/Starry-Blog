package com.star.inf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * 归档
 *
 * @Author: zzStar
 * @Date: 12-19-2020 13:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ArchiveDTO {
    /**
     * id
     */
    private Integer id;

    /**
     * 标题
     */
    private String articleTitle;

    /**
     * 发表时间
     */
    private LocalDateTime createTime;

}
