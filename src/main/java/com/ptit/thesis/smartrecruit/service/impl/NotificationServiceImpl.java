package com.ptit.thesis.smartrecruit.service.impl;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.ptit.thesis.smartrecruit.dto.response.NotificationResponse;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.entity.Notification;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.NotificationType;
import com.ptit.thesis.smartrecruit.exception.ResourceNotFoundException;
import com.ptit.thesis.smartrecruit.mapper.NotificationMapper;
import com.ptit.thesis.smartrecruit.repository.CandidateProfileRepository;
import com.ptit.thesis.smartrecruit.repository.CompanyRepository;
import com.ptit.thesis.smartrecruit.repository.NotificationRepository;
import com.ptit.thesis.smartrecruit.service.NotificationService;
import com.ptit.thesis.smartrecruit.service.S3Service;
import com.ptit.thesis.smartrecruit.utils.Constant;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class NotificationServiceImpl implements NotificationService {

    NotificationRepository notificationRepository;
    CandidateProfileRepository candidateProfileRepository;
    CompanyRepository companyRepository;

    SimpMessagingTemplate messagingTemplate;

    NotificationMapper notificationMapper;

    S3Service s3Service;

    @Override
    @Transactional
    public void pushNotification(User sender, User recipient, String content, NotificationType type, Long relatedId) {
        Notification notification = new Notification();
        notification.setSender(sender);
        notification.setRecipient(recipient);
        notification.setContent(content);
        notification.setType(type);
        notification.setRelatedId(relatedId);
        notification.setIsRead(false);

        Notification savedNotification = notificationRepository.save(notification);

        NotificationResponse notificationResponse = getNotificationResponseFromEntity(savedNotification);

        messagingTemplate.convertAndSendToUser(recipient.getUsername(), "/queue/notifications", notificationResponse);
    }

    @Override
    public Long countUnread(User user) {
        return notificationRepository.countByRecipientIdAndIsReadFalse(user.getId());
    }

    @Override
    public Slice<NotificationResponse> getNotifications(User user, Pageable pageable, Boolean isRead) {
        return notificationRepository.findByRecipientIdAndIsReadOrderByCreatedAtDesc(user.getId(), isRead, pageable)
                .map(
                        notification -> getNotificationResponseFromEntity(notification));
    }

    @Override
    @Transactional
    public void markAsRead(Long notificationId, User user) {
        log.info("Marking notification with id " + notificationId + " as read.");
        Notification notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new ResourceNotFoundException("Notification not found for id: " + notificationId));

        validateUserNotification(user, notificationId);

        notification.setIsRead(true);
        notificationRepository.save(notification);

        log.info("Marking notification with id " + notificationId + " as read successfully.");
    }

    @Override
    public void markReadAll(User user) {
        List<Notification> notifications = notificationRepository.findByRecipientIdAndIsReadFalse(user.getId());
        notifications.forEach(notification -> notification.setIsRead(true));
        notificationRepository.saveAll(notifications);
    }

    private void validateUserNotification(User user, Long notificationId) {
        log.info("Check valid if notification with id " + notificationId + " belongs to user with id " + user.getId());
        if (!notificationRepository.existsByIdAndRecipientId(notificationId, user.getId())) {
            log.warn("This notification does not belong to user.");
            throw new AccessDeniedException("This notification does not belong to you.");
        }
        log.info("This notification belong to user.");
    }

    private NotificationResponse getNotificationResponseFromEntity(Notification notification) {
        NotificationResponse notificationResponse = notificationMapper.toNotificationResponse(notification);
        User sender = notification.getSender();
        if (sender != null) {
            boolean isCandidateSend = sender.getRole().getName().equals(Constant.CANDIDATE_ROLE);
            if (isCandidateSend) {
                CandidateProfile candidate = candidateProfileRepository.findByUser(sender).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Can not found candidatte profile with user id: " + sender.getId()));
                notificationResponse.setSenderAvatarUrl(candidate.getAvatarUrl());
                notificationResponse.setSenderName(candidate.getFullName());
            } else {
                Company company = companyRepository.findByUser(sender).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "Can not found company profile with user id: " + sender.getId()));
                notificationResponse.setSenderAvatarUrl(company.getLogoUrl());
                notificationResponse.setSenderName(company.getName());
            }
            notificationResponse.setSenderId(sender.getId());
            notificationResponse
                    .setSenderAvatarUrl(s3Service.generatePresignedUrl(notificationResponse.getSenderAvatarUrl()));
        }

        return notificationResponse;
    }
}
