package com.backend.dto.request.chatbot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProcessQueryDTO {
    private Long queryId;
    private String intentName;
    private String responseText;
    private boolean requiresDbQuery = false;
    private String queryTemplate;
}