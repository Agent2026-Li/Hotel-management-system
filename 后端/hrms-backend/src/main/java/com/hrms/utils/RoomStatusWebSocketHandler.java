package com.hrms.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrms.dto.response.RoomResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 房态 WebSocket 推送处理器，用于在房态变化后通知前端。
 */
@Component
public class RoomStatusWebSocketHandler extends TextWebSocketHandler {
    private final Set<WebSocketSession> sessions = ConcurrentHashMap.newKeySet();
    private final ObjectMapper objectMapper = new ObjectMapper().findAndRegisterModules();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.add(session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session);
    }

    public void broadcastRoom(RoomResponse room) {
        try {
            String payload = objectMapper.writeValueAsString(Map.of("type", "room-status", "room", room));
            for (WebSocketSession session : sessions) {
                if (session.isOpen()) {
                    session.sendMessage(new TextMessage(payload));
                }
            }
        } catch (Exception ignored) {
            // WebSocket 推送失败不阻断主业务接口。
        }
    }
}
