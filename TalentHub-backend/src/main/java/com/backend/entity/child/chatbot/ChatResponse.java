package com.backend.entity.child.chatbot;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.AbstractEntity;

@Entity
@Table(name = "chat_responses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponse extends AbstractEntity<Long> {

    @Column(name = "response_text", nullable = false, columnDefinition = "TEXT")
    private String responseText;

    @Column(name = "requires_db_query")
    private Boolean requiresDbQuery = false;

    @Column(name = "query_template", columnDefinition = "TEXT")
    private String queryTemplate;

    @Column(name = "display_order")
    private Integer displayOrder = 0;

    @ManyToOne
    @JoinColumn(name = "intent_id")
    @JsonIgnore
    private ChatIntent intent;
}