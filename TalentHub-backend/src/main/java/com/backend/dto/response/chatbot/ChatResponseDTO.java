package com.backend.dto.response.chatbot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.child.chatbot.ChatIntent;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDTO {
    private String message;
    private String detectedIntentName;
    private Float confidence;
    private LocalDateTime timestamp;
    private ChatIntent detectedIntent;

    public String getDetectedIntentName() {
        return detectedIntent != null ? detectedIntent.getIntentName() : null;
    }
}