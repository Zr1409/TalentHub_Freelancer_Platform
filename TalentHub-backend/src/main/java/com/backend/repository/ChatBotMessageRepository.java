package com.backend.repository;

import com.backend.entity.child.chatbot.ChatBotMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatBotMessageRepository extends JpaRepository<ChatBotMessage, Long> {
    List<ChatBotMessage> findByConversationIdOrderBySentAtAsc(Long conversationId);

    @Query("SELECT COUNT(m) FROM ChatBotMessage m")
    Long countTotalMessages();

    @Query("SELECT m.detectedIntent.intentName, COUNT(m) FROM ChatBotMessage m WHERE m.detectedIntent IS NOT NULL GROUP BY m.detectedIntent.intentName ORDER BY COUNT(m) DESC")
    List<Object[]> getPopularIntents();

    @Query("SELECT m FROM ChatBotMessage m WHERE m.detectedIntent IS NULL AND m.isFromBot = false ORDER BY m.sentAt DESC")
    List<ChatBotMessage> findUnrecognizedQueries();
}