package com.backend.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class ChatDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageRequest {
        private Long senderId;
        private Long receiverId;
        private String content;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MessageResponse {
        private Long id;
        private Long senderId;
        private String senderName;
        private String senderAvatar;
        private Long receiverId;
        private String content;
        private LocalDateTime timestamp;
        private boolean isRead;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ConversationSummary {
        private Long userId;
        private String name;
        private String avatar;
        private String lastMessage;
        private LocalDateTime timestamp;
        private int unreadCount;
        private boolean isOnline;
        private String lastSeen;
    }
}