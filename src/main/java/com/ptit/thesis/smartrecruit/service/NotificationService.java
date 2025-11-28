package com.ptit.thesis.smartrecruit.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.ptit.thesis.smartrecruit.dto.response.NotificationResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.NotificationType;

public interface NotificationService {
    public void pushNotification(User sender, User recipient, String content, NotificationType type, Long relatedId);
    public Long countUnread(User user);
    public Slice<NotificationResponse> getNotifications(User user, Pageable pageable);
    public void markAsRead(Long notificationId, User user);
    public void markReadAll(User user);
}
