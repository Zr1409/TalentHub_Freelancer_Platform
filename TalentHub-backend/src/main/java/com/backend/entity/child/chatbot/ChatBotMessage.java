package com.backend.entity.child.chatbot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatbot_messages")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatBotMessage extends AbstractEntity<Long> {

    @Column(name = "message_text", nullable = false, columnDefinition = "TEXT")
    private String messageText;

    @Column(name = "is_from_bot", nullable = false)
    private Boolean isFromBot = false;

    @ManyToOne
    @JoinColumn(name = "intent_id")
    private ChatIntent detectedIntent;

    @Column(name = "confidence_score")
    private Float confidenceScore;

    @Column(name = "sent_at")
    @CreationTimestamp
    private LocalDateTime sentAt;

    @ManyToOne
    @JoinColumn(name = "conversation_id")
    @JsonIgnore
    private ChatConversation conversation;
}