package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * @Author: zzStar
 * @Date: 12-10-2021 17:56
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CanalDTO {

    private String type;

    private String table;

    private List<Map<String, String>> data;

}
