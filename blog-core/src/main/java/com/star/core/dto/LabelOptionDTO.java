package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 角色选项DTO
 *
 * @Author: zzStar
 * @Date: 06-15-2021 17:03
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LabelOptionDTO {

    /**
     * 选项id
     */
    private Integer id;

    /**
     * 选项名
     */
    private String labelName;

    /**
     * 子选项
     */
    private List<LabelOptionDTO> children;

}
