package com.star.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.star.domain.vo.TagVO;
import lombok.Data;

import java.util.Date;

/**
 * 标签
 *
 * @Author: zzStar
 * @Date: 12-18-2020 17:40
 */
@Data
@TableName("tb_tag")
public class Tag {

    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 标签名
     */
    private String tagName;

    /**
     * 创建时间
     */
    private Date createTime;

    public Tag(TagVO tagVO) {
        this.id = tagVO.getId();
        this.tagName = tagVO.getTagName();
        this.createTime = this.id == null ? new Date() : null;
    }

    public Tag() {
    }

}
