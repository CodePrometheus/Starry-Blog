package com.star.core.domain.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @Author: zzStar
 * @Date: 03-29-2021 17:51
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("tb_chat_record")
public class ChatRecord {

    /**
     * 主键
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 用户昵称
     */
    private String nickname;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 聊天内容
     */
    private String content;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 用户登录ip
     */
    private String ipAddr;

    /**
     * ip来源
     */
    private String ipSource;

}
