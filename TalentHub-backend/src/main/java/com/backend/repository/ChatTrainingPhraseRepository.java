package com.backend.repository;

import com.backend.entity.child.chatbot.ChatTrainingPhrase;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatTrainingPhraseRepository extends JpaRepository<ChatTrainingPhrase, Long> {
    List<ChatTrainingPhrase> findByIsProcessedFalse();

    List<ChatTrainingPhrase> findByIntentId(Long intentId);

    Optional<ChatTrainingPhrase> findByPhraseText(String phraseText);

    @Query("SELECT p FROM ChatTrainingPhrase p ORDER BY p.frequency DESC")
    List<ChatTrainingPhrase> findMostFrequentPhrases();

    @Query("SELECT p FROM ChatTrainingPhrase p WHERE p.isProcessed = false ORDER BY p.frequency DESC LIMIT :limit")
    List<ChatTrainingPhrase> findTopUnprocessedPhrases(@Param("limit") int limit);
}