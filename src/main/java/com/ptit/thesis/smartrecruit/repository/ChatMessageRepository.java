package com.ptit.thesis.smartrecruit.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.ChatMessage;
import com.ptit.thesis.smartrecruit.entity.Conversation;
import com.ptit.thesis.smartrecruit.enums.MessageDirection;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    Slice<ChatMessage> findByConversationIdOrderByCreatedAtDesc(Long conversationId, Pageable pageable);

    @Query("SELECT COUNT(m) FROM ChatMessage m WHERE m.conversation = :conversation AND m.isRead = false AND m.direction = :direction")
    long countUnreadMessages(Conversation conversation, MessageDirection direction);

    @Query("SELECT m FROM ChatMessage m WHERE m.conversation = :conversation AND m.isRead = false AND m.direction = :direction")
    List<ChatMessage> findUnreadMessages(Conversation conversation, MessageDirection direction);
}
