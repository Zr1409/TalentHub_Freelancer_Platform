package com.backend.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class WebRTCDto {

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignalRequest {
        private Long senderId;
        private Long receiverId;
        private String type; // "offer", "answer", "candidate", "end-call"
        private String sdp;  // Session Description Protocol
        private Object candidate; // ICE Candidate
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignalResponse {
        private Long senderId;
        private String senderName;
        private String senderAvatar;
        private Long receiverId;
        private String type;
        private String sdp;
        private Object candidate;
        private String timestamp;
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ScreenShareRequest {
        private Long senderId;
        private Long receiverId;
        private boolean isSharing;
    }
}