package com.ssafy.cob.domain.chat.server;

import com.ssafy.cob.domain.chat.model.dto.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class WebSocketServer extends TextWebSocketHandler {

    // 채팅방별 세션 저장용 Map
    private final Map<String, Set<WebSocketSession>> roomSessions = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String room = getRoomName(session);
        roomSessions.computeIfAbsent(room, k -> ConcurrentHashMap.newKeySet()).add(session);
        System.out.println("[" + session.getId() + "] 님이 " + room + " 방에 입장했습니다.");
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Message msg = objectMapper.readValue(message.getPayload(), Message.class);

        if ("2".equals(msg.getCode())) {
            String room = getRoomName(session);
            roomSessions.getOrDefault(room, Set.of()).remove(session);
            return;
        }

        String room = getRoomName(session);
        Set<WebSocketSession> sessions = roomSessions.getOrDefault(room, Set.of());

        for (WebSocketSession s : sessions) {
            if (s.isOpen()) {
                s.sendMessage(new TextMessage(objectMapper.writeValueAsString(msg)));
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        String room = getRoomName(session);
        roomSessions.getOrDefault(room, Set.of()).remove(session);
    }

    // 요청 URI에서 방 이름 추출
    private String getRoomName(WebSocketSession session) {
        String uri = Objects.requireNonNull(session.getUri()).toString();
        return uri.substring(uri.lastIndexOf("/") + 1);
    }
}
