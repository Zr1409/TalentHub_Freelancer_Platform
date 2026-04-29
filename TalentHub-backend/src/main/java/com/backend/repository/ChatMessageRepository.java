package com.backend.repository;

import com.backend.entity.child.account.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    // Find messages between two users
    @Query("SELECT m FROM ChatMessage m WHERE " +
            "(m.sender.id = :user1Id AND m.receiver.id = :user2Id) OR " +
            "(m.sender.id = :user2Id AND m.receiver.id = :user1Id) " +
            "ORDER BY m.sentAt ASC")
    List<ChatMessage> findMessagesBetweenUsers(@Param("user1Id") Long user1Id,
                                               @Param("user2Id") Long user2Id);

    // Count unread messages for a user
    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.receiver.id = :userId AND m.read = false")
    int countUnreadMessages(@Param("userId") Long userId);

    // Find recent conversations for a user
    @Query(value = "SELECT DISTINCT " +
            "CASE WHEN m.sender_id = :userId THEN m.receiver_id ELSE m.sender_id END as contact_id " +
            "FROM chat_messages m " +
            "WHERE m.sender_id = :userId OR m.receiver_id = :userId " +
            "GROUP BY contact_id " +
            "ORDER BY MAX(m.sent_at) DESC",
            nativeQuery = true)
    List<Long> findRecentContactIds(@Param("userId") Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE ChatMessage m SET m.read = true WHERE m.receiver.id = :receiverId AND m.sender.id = :senderId AND m.read = false")
    void markMessagesAsRead(@Param("receiverId") Long receiverId, @Param("senderId") Long senderId);
}