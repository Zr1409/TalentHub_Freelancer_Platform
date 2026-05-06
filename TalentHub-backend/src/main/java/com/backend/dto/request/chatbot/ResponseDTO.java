package com.backend.dto.request.chatbot;

import lombok.Data;

@Data
public class ResponseDTO {
    private String responseText;
    private boolean requiresDbQuery = false;
    private String queryTemplate;
    private Integer displayOrder;
}