package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zzStar
 * @Date: 06-04-2021 10:08
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmailDTO implements Serializable {

    /**
     * 邮箱号
     */
    private String email;

    private String subject;

    private String content;

}
