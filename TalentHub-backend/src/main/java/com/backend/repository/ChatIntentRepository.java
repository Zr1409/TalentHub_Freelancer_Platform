package com.backend.repository;

import com.backend.entity.child.chatbot.ChatIntent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatIntentRepository extends JpaRepository<ChatIntent, Long> {
    Optional<ChatIntent> findByIntentName(String intentName);
}