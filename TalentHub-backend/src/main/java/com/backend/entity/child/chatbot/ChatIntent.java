package com.backend.entity.child.chatbot;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;

import java.util.List;

@Entity
@Table(name = "chat_intents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatIntent extends AbstractEntity<Long> {

    @Column(name = "intent_name", nullable = false, unique = true)
    private String intentName;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "intent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatTrainingPhrase> trainingPhrases;

    @OneToMany(mappedBy = "intent", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ChatResponse> responses;
}