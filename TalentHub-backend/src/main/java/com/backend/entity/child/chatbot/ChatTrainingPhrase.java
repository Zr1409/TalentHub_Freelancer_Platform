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
@Table(name = "chat_training_phrases")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatTrainingPhrase extends AbstractEntity<Long> {

    @Column(name = "phrase_text", nullable = false)
    private String phraseText;

    @Column(name = "is_processed", nullable = false)
    private Boolean isProcessed = false;

    @Column(name = "frequency")
    private Integer frequency = 1;

    @Column(name = "created_at")
    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "intent_id")
    @JsonIgnore
    private ChatIntent intent;
}