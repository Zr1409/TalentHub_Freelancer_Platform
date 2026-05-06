package com.backend.dto.request.chatbot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatBotMessageDTO {
    private String sessionId;
    private String message;
    private Long userId;
}