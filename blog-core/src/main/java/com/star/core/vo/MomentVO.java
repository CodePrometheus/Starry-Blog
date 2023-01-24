package com.star.core.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author: zzStar
 * @Date: 2022/1/12 11:23 PM
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MomentVO {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 内容
     */
    private String momentContent;

    /**
     * 是否置顶
     */
    private Integer isTop;

    /**
     * 状态 0.公开 1.私密
     */
    @NotNull(message = "动态状态不能为空")
    private Integer status;

    /**
     * 图片
     */
    @ApiModelProperty(name = "images", value = "说说图片", dataType = "String")
    private String images;

}
