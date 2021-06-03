package com.star.common.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 树洞类型
 *
 * @Author: zzStar
 * @Date: 03-29-2021 18:02
 */
@Getter
@AllArgsConstructor
public enum WebsocketTypeConst {

    /**
     * 在线人数
     */
    ONLINE_COUNT(1, "在线人数"),

    /**
     * 历史记录
     */
    HISTORY_RECORD(2, "历史记录"),

    /**
     * 发送消息
     */
    SEND_MESSAGE(3, "发送消息"),

    /**
     * 撤回消息
     */
    RECALL_MESSAGE(4, "撤回消息"),

    /**
     * 语音消息
     */
    VOICE_MESSAGE(5, "语音消息"),

    /**
     * 心跳
     */
    HEART_BEAT(6, "心跳消息");

    /**
     * 类型
     */
    private final Integer type;

    /**
     * 描述
     */
    private final String desc;

    /**
     * 根据类型获取枚举
     *
     * @param type 类型
     * @return 枚举
     */
    public static WebsocketTypeConst getWebsocketType(Integer type) {
        for (WebsocketTypeConst webSocketType : WebsocketTypeConst.values()) {
            if (webSocketType.getType().equals(type)) {
                return webSocketType;
            }
        }
        return null;
    }

}
