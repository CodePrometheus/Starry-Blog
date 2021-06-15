package com.star.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.star.core.domain.vo.CategoryVO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * 文章归档
 *
 * @Author: zzStar
 * @Date: 12-18-2020 17:24
 */
@Data
@Builder
@AllArgsConstructor
@TableName("tb_category")
public class Category {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 分类名
     */
    private String categoryName;

    /**
     * 创建时间
     */
    private Date createTime;

}
