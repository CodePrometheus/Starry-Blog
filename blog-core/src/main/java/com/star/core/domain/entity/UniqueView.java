package com.star.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * @Author: zzStar
 * @Date: 12-18-2020 17:41
 */
@Data
@TableName("tb_unique_view")
public class UniqueView {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 时间
     */
    private Date createTime;

    /**
     * 访问量
     */
    private Integer viewsCount;

    public UniqueView(Date createTime, Integer viewsCount) {
        this.createTime = createTime;
        this.viewsCount = viewsCount;
    }

    public UniqueView() {
    }

}
