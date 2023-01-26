package com.star.inf.service;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.common.tool.DateUtil;
import com.star.common.tool.HtmlUtils;
import com.star.common.tool.IpUtils;
import com.star.inf.dto.ChatRecordDTO;
import com.star.inf.dto.RecallMessageDTO;
import com.star.inf.dto.WebsocketMessageDTO;
import com.star.inf.entity.ChatRecord;
import com.star.inf.mapper.ChatRecordMapper;
import jakarta.websocket.*;
import jakarta.websocket.server.HandshakeRequest;
import jakarta.websocket.server.ServerEndpoint;
import jakarta.websocket.server.ServerEndpointConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.star.common.constant.WebsocketTypeConst.*;

/**
 * @Author: zzStar
 * @Date: 03-29-2021 17:44
 */
@Service
@ServerEndpoint(value = "/websocket", configurator = WebSocketServiceImpl.ChatConfigurator.class)
public class WebSocketServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(WebSocketServiceImpl.class);

    private static ChatRecordMapper recordMapper;

    /**
     * Session
     */
    private Session session;

    /**
     * 用户session集合
     * 线程安全的无序的集合，可以将它理解成线程安全的HashSet
     */
    private static final CopyOnWriteArraySet<WebSocketServiceImpl> webSocketSet = new CopyOnWriteArraySet<>();

    @Autowired
    public void setRecordMapper(ChatRecordMapper recordMapper) {
        WebSocketServiceImpl.recordMapper = recordMapper;
    }

    /**
     * 连接成功call
     *
     * @param session
     * @param endpointConfig
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) throws IOException {
        // 加入连接
        this.session = session;
        webSocketSet.add(this);
        // 更新在线人数
        updateOnlineCount();
        // 加载历史聊天记录
        ChatRecordDTO chatRecordDTO = listChatRecords(endpointConfig);

        // 发送消息 历史记录
        WebsocketMessageDTO messageDTO = WebsocketMessageDTO.builder()
                .type(HISTORY_RECORD.getType())
                .data(chatRecordDTO).build();
        synchronized (session) {
            session.getBasicRemote().sendText(JSON.toJSONString(messageDTO));
        }
    }

    /**
     * 加载历史聊天记录
     *
     * @param endpointConfig 配置
     * @return 加载历史聊天记录
     */
    private ChatRecordDTO listChatRecords(EndpointConfig endpointConfig) {
        List<ChatRecord> chatRecordList = recordMapper.selectList(new LambdaQueryWrapper<ChatRecord>()
                .ge(ChatRecord::getCreateTime, DateUtil.getBeforeHourTime(12)));
        String ipaddr = endpointConfig.getUserProperties().get(ChatConfigurator.HEADER_NAME).toString();
        return ChatRecordDTO.builder()
                .chatRecordList(chatRecordList)
                .ipAddr(ipaddr)
                .ipSource(IpUtils.getIpSource(ipaddr)).build();
    }

    /**
     * 更新在线人数
     */
    private void updateOnlineCount() throws IOException {
        WebsocketMessageDTO messageDTO = WebsocketMessageDTO.builder()
                .type(ONLINE_COUNT.getType())
                .data(webSocketSet.size()).build();
        for (WebSocketServiceImpl webSocketService : webSocketSet) {
            synchronized (webSocketService.session) {
                webSocketService.session.getBasicRemote().sendText(JSON.toJSONString(messageDTO));
            }
        }
    }

    /**
     * 获取ip
     */
    public static class ChatConfigurator extends ServerEndpointConfig.Configurator {
        public static String HEADER_NAME = "X-Real-IP";

        public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
            try {
                String firstHeader = request.getHeaders().get(HEADER_NAME.toLowerCase()).get(0);
                sec.getUserProperties().put(HEADER_NAME, firstHeader);
            } catch (Exception e) {
                sec.getUserProperties().put(HEADER_NAME, "未知ip");
            }
        }
    }


    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message) throws IOException {
        WebsocketMessageDTO msg = JSON.parseObject(message, WebsocketMessageDTO.class);
        switch (Objects.requireNonNull(getWebsocketType(msg.getType()))) {

            // 发送消息
            case SEND_MESSAGE:
                ChatRecord chatRecord = JSON.parseObject(JSON.toJSONString(msg.getData()), ChatRecord.class);
                // 过滤html标签
                chatRecord.setContent(HtmlUtils.deleteCommentTag(chatRecord.getContent()));
                recordMapper.insert(chatRecord);
                msg.setData(chatRecord);
                for (WebSocketServiceImpl webSocketService : webSocketSet) {
                    synchronized (webSocketService.session) {
                        webSocketService.session.getBasicRemote().sendText(JSON.toJSONString(msg));
                    }
                }
                break;

            // 撤回消息
            case RECALL_MESSAGE:
                RecallMessageDTO recallMsg = JSON.parseObject(JSON.toJSONString(msg.getData()), RecallMessageDTO.class);
                // 删除记录
                deleteRecord(recallMsg.getId());
                for (WebSocketServiceImpl webSocketService : webSocketSet) {
                    synchronized (webSocketService.session) {
                        webSocketService.session.getBasicRemote().sendText(JSON.toJSONString(msg));
                    }
                }
                break;

            // 心跳消息
            case HEART_BEAT:
                msg.setData("pong");
                session.getBasicRemote().sendText(JSON.toJSONString(JSON.toJSONString(msg)));

            default:
                break;
        }
    }

    /**
     * 删除聊天记录
     *
     * @param id ID
     */
    @Async
    public void deleteRecord(Integer id) {
        recordMapper.deleteById(id);
    }

    /**
     * 关闭连接调用
     */
    @OnClose
    public void onClose() throws IOException {
        // 在线人数清零
        webSocketSet.remove(this);
        updateOnlineCount();
    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Throwable throwable) {
        throwable.printStackTrace();
        log.error("websocket发生错误: ", throwable);
    }

}
