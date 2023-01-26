package com.star.inf.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zzStar
 * @Date: 08-15-2021 21:26
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel(description = "博客信息")
public class BlogInfoVo {

    /**
     * 关于我内容
     */
    @ApiModelProperty(name = "aboutContent", value = "关于我内容", required = true, dataType = "String")
    private String aboutContent;

}
