package com.ssafy.cob.config;

import com.ssafy.cob.domain.chat.server.WebSocketServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        // 채팅방 이름 동적으로 받도록 endpoint 구성
        registry.addHandler(new WebSocketServer(), "/chatserver/{roomName}")
                .setAllowedOrigins("*");
    }
}
