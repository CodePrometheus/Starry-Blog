package com.star.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.star.domain.entity.ChatRecord;
import com.star.domain.mapper.ChatRecordMapper;
import com.star.tool.DateUtil;
import com.star.tool.IpUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.websocket.*;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpoint;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import static com.star.constant.WebsocketTypeConst.*;

/**
 * @Author: zzStar
 * @Date: 03-29-2021 17:44
 */
@Slf4j
@Service
@ServerEndpoint(value = "/websocket", configurator = WebSocketServiceImpl.ChatConfigurator.class)
public class WebSocketServiceImpl {

    private static ChatRecordMapper recordMapper;

    /**
     * Session
     */
    private Session session;


    /**
     * 线程安全的无序的集合，可以将它理解成线程安全的HashSet
     */
    private static CopyOnWriteArraySet<WebSocketServiceImpl> webSocketSet = new CopyOnWriteArraySet<>();

    @Autowired
    public void setRecordMapper(ChatRecordMapper recordMapper) {
        WebSocketServiceImpl.recordMapper = recordMapper;
    }

    /**
     * 连接成功
     *
     * @param session
     * @param endpointConfig
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) throws IOException {
        this.session = session;
        webSocketSet.add(this);
        // 更新在线人数
        updateOnlineCount();
        // 加载历史聊天记录
        LambdaQueryWrapper<ChatRecord> wrapper = new LambdaQueryWrapper<ChatRecord>().ge(ChatRecord::getCreateTime, DateUtil.getBeforeHourTime(12));
        List<ChatRecord> chatRecordList = recordMapper.selectList(wrapper);

        Map<String, Object> recordMap = new HashMap<>(16);
        String ipAddr = endpointConfig.getUserProperties().get(ChatConfigurator.HEADER_NAME).toString();
        recordMap.put("chatRecordList", chatRecordList);
        recordMap.put("ipAddr", ipAddr);
        recordMap.put("ipSource", IpUtil.getIpSource(ipAddr));
        recordMap.put("type", HISTORY_RECORD.getType());
        synchronized (session) {
            session.getBasicRemote().sendText(JSON.toJSONString(recordMap));
        }
    }

    /**
     * 更新在线人数
     */
    private void updateOnlineCount() throws IOException {
        Map<String, Object> count = new ConcurrentHashMap<>(16);
        count.put("count", webSocketSet.size());
        count.put("type", ONLINE_COUNT.getType());
        for (WebSocketServiceImpl webSocketService : webSocketSet) {
            // 使用websocket同步发送，并对使用的session加同步锁synchronized
            synchronized (webSocketService.session) {
                webSocketService.session.getBasicRemote().sendText(JSON.toJSONString(count));
            }
        }
    }

    /**
     * 获取ip
     */
    public static class ChatConfigurator extends ServerEndpointConfig.Configurator {
        public static String HEADER_NAME = "X-Real-IP";

        @Override
        public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
            try {
                String firstHeader = request.getHeaders().get(HEADER_NAME.toLowerCase()).get(0);
                sec.getUserProperties().put(HEADER_NAME, firstHeader);
            } catch (Exception e) {
                sec.getUserProperties().put(HEADER_NAME, "未知IP");
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
        Map data = JSON.parseObject(message, Map.class);
        switch (Objects.requireNonNull(getWebsocketType((Integer) data.get("type")))) {
            // 发送消息
            case SEND_MESSAGE:
                ChatRecord chatRecord = JSON.parseObject(JSON.toJSONString(data), ChatRecord.class);
                recordMapper.insert(chatRecord);
                data.put("id", chatRecord.getId());
                for (WebSocketServiceImpl webSocketService : webSocketSet) {
                    synchronized (webSocketService.session) {
                        webSocketService.session.getBasicRemote().sendText(JSON.toJSONString(data));
                    }
                }
                break;

            // 撤回消息
            case RECALL_MESSAGE:
                Integer id = (Integer) data.get("id");
                // 删除记录
                deleteRecord(id);
                for (WebSocketServiceImpl webSocketService : webSocketSet) {
                    synchronized (webSocketService.session) {
                        webSocketService.session.getBasicRemote().sendText(message);
                    }
                }
                break;

            default:
                break;
        }
    }

    /**
     * 删除聊天记录
     *
     * @param id
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
    }



}
