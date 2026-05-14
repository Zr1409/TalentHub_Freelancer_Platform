package com.backend.controller;

import com.backend.dto.ResponseObject;
import com.backend.dto.request.chatbot.ChatBotMessageDTO;
import com.backend.dto.request.chatbot.IntentDTO;
import com.backend.dto.request.chatbot.ProcessQueryDTO;
import com.backend.dto.request.chatbot.ResponseDTO;
import com.backend.dto.response.chatbot.ChatResponseDTO;
import com.backend.entity.child.chatbot.ChatIntent;
import com.backend.entity.child.chatbot.ChatBotMessage;
import com.backend.entity.child.chatbot.ChatResponse;
import com.backend.entity.child.chatbot.ChatTrainingPhrase;
import com.backend.repository.ChatIntentRepository;
import com.backend.repository.ChatResponseRepository;
import com.backend.service.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/chatbot")
public class ChatbotController {

    @Autowired
    private ChatbotService chatbotService;

    @Autowired
    private ChatIntentRepository chatIntentRepository;

    @Autowired
    private ChatResponseRepository chatResponseRepository;

    /**
     * Process a user message and get a response
     */
    @PostMapping("/message")
    public ResponseObject<ChatResponseDTO> processMessage(@RequestBody ChatBotMessageDTO messageDTO) {
        ChatBotMessage botResponse = chatbotService.processUserMessage(
                messageDTO.getSessionId(),
                messageDTO.getMessage(),
                messageDTO.getUserId()
        );

        ChatResponseDTO responseDTO = new ChatResponseDTO();
        responseDTO.setMessage(botResponse.getMessageText());
        responseDTO.setDetectedIntent(botResponse.getDetectedIntent());
        responseDTO.setConfidence(botResponse.getConfidenceScore());
        responseDTO.setTimestamp(botResponse.getSentAt());

        return ResponseObject.<ChatResponseDTO>builder()
                .data(responseDTO)
                .message("Message processed successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Get unprocessed queries for training
     */
    @GetMapping("/training/unprocessed")
    public ResponseObject<List<Map<String, Object>>> getUnprocessedQueries(
            @RequestParam(defaultValue = "10") int limit
    ) {
        List<ChatTrainingPhrase> unprocessedQueries = chatbotService.getUnprocessedQueries(limit);

        List<Map<String, Object>> result = unprocessedQueries.stream()
                .map(query -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", query.getId());
                    map.put("text", query.getPhraseText());
                    map.put("frequency", query.getFrequency());
                    map.put("createdAt", query.getCreatedAt());
                    ChatBotMessage message = chatbotService.findOriginalMessage(query.getPhraseText());
                    if (message != null && message.getConversation() != null) {
                        map.put("userId", message.getConversation().getUser() != null ?
                                message.getConversation().getUser().getId() : null);
                        map.put("sessionId", message.getConversation().getSessionId());
                        map.put("timestamp", message.getSentAt());
                        if (message.getConversation().getUser() != null &&
                                message.getConversation().getUser().getAccount() != null) {
                            map.put("userEmail", message.getConversation().getUser().getAccount().getEmail());
                        }
                    }
                    return map;
                })
                .collect(Collectors.toList());

        return ResponseObject.<List<Map<String, Object>>>builder()
                .data(result)
                .message("Unprocessed queries retrieved successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Process an unrecognized query
     */
    @PostMapping("/training/process-query")
    public ResponseObject<Void> processQuery(@RequestBody ProcessQueryDTO processQueryDTO) {
        chatbotService.processUnrecognizedQuery(
                processQueryDTO.getQueryId(),
                processQueryDTO.getIntentName(),
                processQueryDTO.getResponseText(),
                processQueryDTO.isRequiresDbQuery(),
                processQueryDTO.getQueryTemplate()
        );

        return ResponseObject.<Void>builder()
                .data(null)
                .message("Query processed successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Add a new intent with training phrases and responses
     */
    @PostMapping("/intent")
    public ResponseObject<ChatIntent> addIntent(@RequestBody IntentDTO intentDTO) {
        ChatIntent intent = chatbotService.addIntent(
                intentDTO.getIntentName(),
                intentDTO.getDescription(),
                intentDTO.getTrainingPhrases(),
                intentDTO.getResponses(),
                intentDTO.isRequiresDbQuery(),
                intentDTO.getDbQuery()
        );

        return ResponseObject.<ChatIntent>builder()
                .data(intent)
                .message("Intent added successfully")
                .status(HttpStatus.CREATED.value())
                .build();
    }

    /**
     * Get intent details by ID
     */
    @GetMapping("/intent/{id}")
    public ResponseObject<Map<String, Object>> getIntentDetails(@PathVariable Long id) {
        Map<String, Object> intentDetails = chatbotService.getIntentDetails(id);

        return ResponseObject.<Map<String, Object>>builder()
                .data(intentDetails)
                .message("Intent details retrieved successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Update an existing intent
     */
    @PutMapping("/intent/{id}")
    public ResponseObject<ChatIntent> updateIntent(
            @PathVariable Long id,
            @RequestBody IntentDTO intentDTO
    ) {
        ChatIntent updatedIntent = chatbotService.updateIntent(
                id,
                intentDTO.getIntentName(),
                intentDTO.getDescription(),
                intentDTO.getTrainingPhrases()
        );

        return ResponseObject.<ChatIntent>builder()
                .data(updatedIntent)
                .message("Intent updated successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Delete an intent
     */
    @DeleteMapping("/intent/{id}")
    public ResponseObject<Void> deleteIntent(@PathVariable Long id) {
        chatbotService.deleteIntent(id);

        return ResponseObject.<Void>builder()
                .data(null)
                .message("Intent deleted successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Get all responses for an intent
     */
    @GetMapping("/intent/{id}/responses")
    public ResponseObject<List<ChatResponse>> getResponses(@PathVariable Long id) {
        List<ChatResponse> responses = chatResponseRepository.findByIntentIdOrderByDisplayOrderAsc(id);

        return ResponseObject.<List<ChatResponse>>builder()
                .data(responses)
                .message("Responses retrieved successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Add a response to an intent
     */
    @PostMapping("/intent/{id}/response")
    public ResponseObject<ChatResponse> addResponse(
            @PathVariable Long id,
            @RequestBody ResponseDTO responseDTO
    ) {
        ChatResponse response = chatbotService.addResponseToIntent(
                id,
                responseDTO.getResponseText(),
                responseDTO.isRequiresDbQuery(),
                responseDTO.getQueryTemplate()
        );

        return ResponseObject.<ChatResponse>builder()
                .data(response)
                .message("Response added successfully")
                .status(HttpStatus.CREATED.value())
                .build();
    }

    /**
     * Update a response
     */
    @PutMapping("/response/{id}")
    public ResponseObject<ChatResponse> updateResponse(
            @PathVariable Long id,
            @RequestBody ResponseDTO responseDTO
    ) {
        ChatResponse response = chatbotService.updateResponse(
                id,
                responseDTO.getResponseText(),
                responseDTO.isRequiresDbQuery(),
                responseDTO.getQueryTemplate()
        );

        return ResponseObject.<ChatResponse>builder()
                .data(response)
                .message("Response updated successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Delete a response
     */
    @DeleteMapping("/response/{id}")
    public ResponseObject<Void> deleteResponse(@PathVariable Long id) {
        chatbotService.deleteResponse(id);

        return ResponseObject.<Void>builder()
                .data(null)
                .message("Response deleted successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Test database query for a response
     */
    @PostMapping("/test-query")
    public ResponseObject<List<Map<String, Object>>> testQuery(@RequestBody String query) {
        List<Map<String, Object>> results = chatbotService.executeTestQuery(query);

        return ResponseObject.<List<Map<String, Object>>>builder()
                .data(results)
                .message("Query executed successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Get chatbot statistics
     */
    @GetMapping("/statistics")
    public ResponseObject<Map<String, Object>> getStatistics() {
        Map<String, Object> statistics = chatbotService.getChatbotStatistics();

        return ResponseObject.<Map<String, Object>>builder()
                .data(statistics)
                .message("Statistics retrieved successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Get all available intents
     */
    @GetMapping("/intents")
    public ResponseObject<List<ChatIntent>> getAllIntents() {
        List<ChatIntent> intents = chatIntentRepository.findAll();

        return ResponseObject.<List<ChatIntent>>builder()
                .data(intents)
                .message("All intents retrieved successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Update chatbot configuration settings
     */
    @PostMapping("/settings")
    public ResponseObject<Void> updateSettings(@RequestBody Map<String, Object> settings) {
        chatbotService.updateSettings(settings);

        return ResponseObject.<Void>builder()
                .data(null)
                .message("Settings updated successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Get current chatbot configuration settings
     */
    @GetMapping("/settings")
    public ResponseObject<Map<String, Object>> getSettings() {
        Map<String, Object> settings = chatbotService.getSettings();

        return ResponseObject.<Map<String, Object>>builder()
                .data(settings)
                .message("Settings retrieved successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Lấy danh sách các ý định gợi ý
     */
    @GetMapping("/suggestions/intents")
    public ResponseObject<List<Map<String, Object>>> getSuggestedIntents() {
        List<Map<String, Object>> suggestedIntents = chatbotService.getSuggestedIntents();

        return ResponseObject.<List<Map<String, Object>>>builder()
                .data(suggestedIntents)
                .message("Suggested intents retrieved successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Lấy câu hỏi gợi ý cho một ý định
     */
    @GetMapping("/suggestions/questions/{intentId}")
    public ResponseObject<List<String>> getSuggestedQuestions(@PathVariable Long intentId) {
        List<String> suggestedQuestions = chatbotService.getSuggestedQuestionsForIntent(intentId);

        return ResponseObject.<List<String>>builder()
                .data(suggestedQuestions)
                .message("Suggested questions retrieved successfully")
                .status(HttpStatus.OK.value())
                .build();
    }

    /**
     * Lấy tất cả gợi ý cho chatbot
     */
    @GetMapping("/suggestions")
    public ResponseObject<Map<String, Object>> getAllSuggestions() {
        Map<String, Object> allSuggestions = new HashMap<>();

        // Lấy danh sách các ý định
        List<Map<String, Object>> intentsList = chatbotService.getSuggestedIntents();
        allSuggestions.put("intents", intentsList);

        // Lấy câu hỏi gợi ý cho mỗi ý định
        Map<String, List<String>> questionsByIntent = new HashMap<>();
        for (Map<String, Object> intent : intentsList) {
            Long intentId = ((Number) intent.get("id")).longValue();
            List<String> questions = chatbotService.getSuggestedQuestionsForIntent(intentId);
            questionsByIntent.put(intentId.toString(), questions);
        }
        allSuggestions.put("questionsByIntent", questionsByIntent);

        return ResponseObject.<Map<String, Object>>builder()
                .data(allSuggestions)
                .message("All suggestions retrieved successfully")
                .status(HttpStatus.OK.value())
                .build();
    }
}