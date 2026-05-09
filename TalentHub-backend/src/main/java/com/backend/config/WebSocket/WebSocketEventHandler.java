package com.backend.config.WebSocket;


import lombok.RequiredArgsConstructor;
import com.backend.service.chat.ChatService;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Map;

@Component
@RequiredArgsConstructor
public class WebSocketEventHandler {

    private final ChatService chatService;

    //    @EventListener
//    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
//        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
//        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
//        System.out.println("userId" + userId);
//        if (userId != null) {
//            chatService.userConnected(userId);
//        }
//    }
    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Map<String, Object> sessionAttributes = headerAccessor.getSessionAttributes();
        Long userId = null;
        if (sessionAttributes != null) {
            userId = (Long) sessionAttributes.get("userId");
        }
        System.out.println("userId: " + userId);
        if (userId != null) {
            chatService.userConnected(userId);
        } else {
            System.out.println("No userId found in session attributes");
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        Long userId = (Long) headerAccessor.getSessionAttributes().get("userId");
        if (userId != null) {
            chatService.userDisconnected(userId);
        }
    }
}