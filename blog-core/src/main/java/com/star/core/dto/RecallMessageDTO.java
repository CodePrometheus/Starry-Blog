package com.star.core.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 撤回的消息
 *
 * @Author: zzStar
 * @Date: 06-01-2021 17:30
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RecallMessageDTO {

    /**
     * 消息id
     */
    private Integer id;

    /**
     * 是否为语音
     */
    private Boolean isVoice;

}
