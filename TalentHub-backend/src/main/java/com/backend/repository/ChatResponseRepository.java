package com.backend.repository;

import com.backend.entity.child.chatbot.ChatResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatResponseRepository extends JpaRepository<ChatResponse, Long> {
    List<ChatResponse> findByIntentIdOrderByDisplayOrderAsc(Long intentId);

    @Query("SELECT r FROM ChatResponse r WHERE r.intent.intentName = :intentName ORDER BY r.displayOrder")
    List<ChatResponse> findByIntentName(@Param("intentName") String intentName);
}