package com.backend.dto.request.chatbot;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IntentDTO {
    private String intentName;
    private String description;
    private List<String> trainingPhrases;
    private List<String> responses;
    private boolean requiresDbQuery = false;
    private String dbQuery;
}