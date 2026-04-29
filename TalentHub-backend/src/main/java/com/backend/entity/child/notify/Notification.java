package com.backend.entity.child.notify;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.PrePersist;
import lombok.Data;
import com.backend.entity.AbstractEntity;
import java.time.LocalDateTime;

@Entity
@Data
public class Notification extends AbstractEntity<Long> {
    @Column(name="message")
    private String message;
    @Column(name="user_id")
    private Long userId;
    private boolean isRead;
    private String url;
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}