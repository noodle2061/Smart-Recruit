package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.MessageDirection;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "chat_messages")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChatMessage extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    Conversation conversation;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    MessageDirection direction;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    String content;
    
    @Column(nullable = false)
    Boolean isRead;
}
