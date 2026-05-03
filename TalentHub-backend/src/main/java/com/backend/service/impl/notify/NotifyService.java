package com.backend.service.impl.notify;
import lombok.RequiredArgsConstructor;
import com.backend.entity.child.notify.Notification;
import com.backend.repository.NotificationRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotifyService {

    private final NotificationRepository notificationRepository;
    private final SimpMessagingTemplate messagingTemplate;

    @Transactional
    public void sendNotification(Long userId, String message, String url) {
        Notification notification = new Notification();
        notification.setUserId(userId);
        notification.setMessage(message);
        notification.setRead(false);
        notification.setUrl(url);
        notificationRepository.save(notification);

        messagingTemplate.convertAndSend("/user/" + userId + "/queue/notifications", notification);
    }

    @Transactional
    public void sendBroadcastNotification(String message) {
        Notification notification = new Notification();
        notification.setMessage(message);
        notification.setRead(false);

        notificationRepository.save(notification);

        messagingTemplate.convertAndSend(
                "/topic/notifications",
                notification
        );
    }

    public List<Notification> getUserNotifications(Long userId) {
        return notificationRepository.findByUserIdOrderByCreatedAtDesc(userId);
    }

    @Transactional
    public void markAsRead(Long notificationId) {
        notificationRepository.findById(notificationId)
                .ifPresent(notification -> {
                    notification.setRead(true);
                    notificationRepository.save(notification);
                });
    }

    @Transactional
    public void markAllAsRead(Long userId) {
        List<Notification> notifications = notificationRepository.findByUserIdAndIsReadFalse(userId);
        notifications.forEach(notification -> {
            notification.setRead(true);
            notificationRepository.save(notification);
        });
    }

    public long countUnreadNotifications(Long userId) {
        return notificationRepository.countByUserIdAndIsReadFalse(userId);
    }
}