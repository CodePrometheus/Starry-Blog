package com.star.core.dto;

import lombok.Data;

import java.util.Date;

/**
 * 归档
 *
 * @Author: zzStar
 * @Date: 12-19-2020 13:40
 */
@Data
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
    private Date createTime;

}
