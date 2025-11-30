package com.ptit.thesis.smartrecruit.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.ptit.thesis.smartrecruit.dto.request.ChatMessageRequest;
import com.ptit.thesis.smartrecruit.dto.response.ChatMessageResponse;
import com.ptit.thesis.smartrecruit.dto.response.ChatMessagesInitialResponse;
import com.ptit.thesis.smartrecruit.dto.response.ConversationResponse;
import com.ptit.thesis.smartrecruit.entity.User;

public interface ChatService {
    ChatMessagesInitialResponse sendMessage(ChatMessageRequest request, User sender, boolean isInitialized);

    Slice<ConversationResponse> getUserConversations(User user, Pageable pageable, Boolean isRead, String keyword);

    Slice<ChatMessageResponse> getConversationMessages(Long conversationId, User user, Pageable pageable);

    void markAsRead(Long conversationId, User user);
}
