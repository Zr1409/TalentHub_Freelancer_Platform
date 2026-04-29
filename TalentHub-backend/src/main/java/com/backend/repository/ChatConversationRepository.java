package com.backend.repository;

import com.backend.entity.child.chatbot.ChatConversation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ChatConversationRepository extends JpaRepository<ChatConversation, Long> {
    List<ChatConversation> findByUserId(Long userId);

    List<ChatConversation> findBySessionId(String sessionId);

    @Query("SELECT COUNT(c) FROM ChatConversation c")
    Long countTotalConversations();

    @Query("SELECT AVG(c.satisfactionRating) FROM ChatConversation c WHERE c.satisfactionRating IS NOT NULL")
    Double getAverageSatisfactionRating();

    List<ChatConversation> findByStartedAtBetween(LocalDateTime start, LocalDateTime end);
}