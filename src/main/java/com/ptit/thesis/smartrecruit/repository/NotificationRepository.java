package com.ptit.thesis.smartrecruit.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.Notification;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, Long> {

    List<Notification> findByRecipientIdAndIsReadFalse(Long recipientId);


    @Query("SELECT n FROM Notification n " +
        "WHERE (n.recipient.id = :recipientId " + 
        "AND (:isRead IS NULL OR n.isRead = :isRead)) " +
        "ORDER BY n.createdAt DESC"
    )
    Slice<Notification> findByRecipientIdAndIsReadOrderByCreatedAtDesc(Long recipientId, Boolean isRead, Pageable pageable);

    long countByRecipientIdAndIsReadFalse(Long recipientId);

    boolean existsByIdAndRecipientId(Long notificationId, Long recipientId);
}
