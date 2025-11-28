package com.ptit.thesis.smartrecruit.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.ptit.thesis.smartrecruit.dto.request.ChatMessageRequest;
import com.ptit.thesis.smartrecruit.dto.response.ChatMessageResponse;
import com.ptit.thesis.smartrecruit.dto.response.ConversationResponse;
import com.ptit.thesis.smartrecruit.entity.User;

public interface ChatService {
    void sendMessage(ChatMessageRequest request, User sender);

    Slice<ConversationResponse> getUserConversations(User user, Pageable pageable);

    Slice<ChatMessageResponse> getConversationMessages(Long conversationId, User user, Pageable pageable);

    void markAsRead(Long conversationId, User user);
}
