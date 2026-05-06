package com.backend.dto.request.chatbot;

import lombok.Data;

@Data
public class ChatbotSettingsDTO {
    private Double confidenceThreshold = 0.7;
    private Integer maxResponses = 1;
    private String fallbackStrategy = "default";
    private boolean enableLearning = true;
}
