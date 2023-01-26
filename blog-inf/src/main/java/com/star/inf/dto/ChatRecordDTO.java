package com.star.inf.dto;

import com.star.inf.entity.ChatRecord;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 记录聊天记录
 *
 * @Author: zzStar
 * @Date: 06-01-2021 17:14
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatRecordDTO {

    private List<ChatRecord> chatRecordList;

    private String ipAddr;

    private String ipSource;

}
