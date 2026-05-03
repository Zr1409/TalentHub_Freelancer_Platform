package com.backend.service.chat;

import lombok.RequiredArgsConstructor;
import com.backend.dto.chat.ChatDto;
import com.backend.dto.chat.WebRTCDto;
import com.backend.entity.child.account.ChatMessage;
import com.backend.entity.child.account.User;
import com.backend.exception.NotFoundException;
import com.backend.repository.ChatMessageRepository;
import com.backend.repository.UserRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatService {

    private final ChatMessageRepository chatMessageRepository;
    private final UserRepository userRepository;
    private final SimpMessagingTemplate messagingTemplate;

    // Track online users
    private final List<Long> onlineUsers = new ArrayList<>();

    // WebRTC signaling handling
    public WebRTCDto.SignalResponse handleSignal(WebRTCDto.SignalRequest request) {
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new NotFoundException("Sender not found with ID: " + request.getSenderId()));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new NotFoundException("Receiver not found with ID: " + request.getReceiverId()));

        // Create response with sender details
        WebRTCDto.SignalResponse response = new WebRTCDto.SignalResponse(
                sender.getId(),
                sender.getFirstName() + " " + sender.getLastName(),
                sender.getImage(),
                receiver.getId(),
                request.getType(),
                request.getSdp(),
                request.getCandidate(),
                LocalDateTime.now().toString()
        );

        // Send signal to recipient via WebSocket
        messagingTemplate.convertAndSend("/queue/call/" + receiver.getId(), response);

        // Return response for REST API calls
        return response;
    }

    public void userConnected(Long userId) {
        if (!onlineUsers.contains(userId)) {
            onlineUsers.add(userId);
        }
        // Notify other users that this user is online
        messagingTemplate.convertAndSend("/topic/status", userId);
    }

    public void userDisconnected(Long userId) {
        onlineUsers.remove(userId);
        // Notify other users that this user is offline
        messagingTemplate.convertAndSend("/topic/status", -userId); // Negative to indicate offline
    }

    public boolean isUserOnline(Long userId) {
        return onlineUsers.contains(userId);
    }

    @Transactional
    public ChatDto.MessageResponse sendMessage(ChatDto.MessageRequest request) {
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new NotFoundException("Sender not found with ID: " + request.getSenderId()));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new NotFoundException("Receiver not found with ID: " + request.getReceiverId()));

        ChatMessage message = new ChatMessage();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(request.getContent());
        message.setSentAt(LocalDateTime.now());
        message.setRead(false);

        chatMessageRepository.save(message);

        // Create response DTO
        ChatDto.MessageResponse response = convertToDto(message);

        // Send to specific user's queue
        messagingTemplate.convertAndSend("/queue/messages/" + receiver.getId(), response);

        return response;
    }

    @Transactional(readOnly = true)
    public List<ChatDto.MessageResponse> getConversation(Long user1Id, Long user2Id) {
        return chatMessageRepository.findMessagesBetweenUsers(user1Id, user2Id)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void markAsRead(Long receiverId, Long senderId) {
        chatMessageRepository.markMessagesAsRead(receiverId, senderId);

        // Notify sender that messages were read
        messagingTemplate.convertAndSend("/queue/read-receipts/" + senderId,
                Map.of("receiverId", receiverId, "senderId", senderId));
    }

    @Transactional(readOnly = true)
    public List<ChatDto.ConversationSummary> getRecentConversations(Long userId) {
        List<Long> contactIds = chatMessageRepository.findRecentContactIds(userId);

        return contactIds.stream().map(contactId -> {
            User contact = userRepository.findById(contactId)
                    .orElseThrow(() -> new NotFoundException("User not found with ID: " + contactId));

            // Get last message
            List<ChatMessage> messages = chatMessageRepository
                    .findMessagesBetweenUsers(userId, contactId);

            ChatMessage lastMessage = messages.isEmpty() ? null : messages.get(messages.size() - 1);

            // Count unread messages
            int unreadCount = (int) messages.stream()
                    .filter(m -> m.getReceiver().getId().equals(userId) && !m.isRead())
                    .count();

            return new ChatDto.ConversationSummary(
                    contact.getId(),
                    contact.getFirstName() + " " + contact.getLastName(),
                    contact.getImage(),
                    lastMessage != null ? lastMessage.getContent() : "",
                    lastMessage != null ? lastMessage.getSentAt() : LocalDateTime.now(),
                    unreadCount,
                    isUserOnline(contactId),
                    isUserOnline(contactId) ? null : "Offline"
            );
        }).collect(Collectors.toList());
    }

    private ChatDto.MessageResponse convertToDto(ChatMessage message) {
        return new ChatDto.MessageResponse(
                message.getId(),
                message.getSender().getId(),
                message.getSender().getFirstName() + " " + message.getSender().getLastName(),
                message.getSender().getImage(),
                message.getReceiver().getId(),
                message.getContent(),
                message.getSentAt(),
                message.isRead()
        );
    }

    public WebRTCDto.SignalResponse handleIceCandidates(WebRTCDto.SignalRequest request) {
        User sender = userRepository.findById(request.getSenderId())
                .orElseThrow(() -> new NotFoundException("Sender not found with ID: " + request.getSenderId()));

        User receiver = userRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new NotFoundException("Receiver not found with ID: " + request.getReceiverId()));

        WebRTCDto.SignalResponse response = new WebRTCDto.SignalResponse(
                sender.getId(),
                sender.getFirstName() + " " + sender.getLastName(),
                sender.getImage(),
                receiver.getId(),
                request.getType(),
                request.getSdp(),
                request.getCandidate(),  // Có thể là một array thay vì một candidate
                LocalDateTime.now().toString()
        );

        // Gửi đến người nhận
        messagingTemplate.convertAndSend("/queue/call/" + receiver.getId(), response);
        return response;
    }
}