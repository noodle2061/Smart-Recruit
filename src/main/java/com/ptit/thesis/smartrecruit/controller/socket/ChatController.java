package com.ptit.thesis.smartrecruit.controller.socket;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ptit.thesis.smartrecruit.dto.request.ChatMessageRequest;
import com.ptit.thesis.smartrecruit.dto.response.ApiResponse;
import com.ptit.thesis.smartrecruit.dto.response.ChatMessageResponse;
import com.ptit.thesis.smartrecruit.dto.response.ChatMessagesInitialResponse;
import com.ptit.thesis.smartrecruit.dto.response.ConversationResponse;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.service.ChatService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;



@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Tag(name = "Chat Controller", description = "Chat: Lấy danh sách chat và hội thoại")
public class ChatController {

    ChatService chatService;

    @PostMapping("/send")
    public ResponseEntity<ApiResponse<ChatMessagesInitialResponse>> sendMessage(@AuthenticationPrincipal User user, ChatMessageRequest request) {
        ChatMessagesInitialResponse chatMessagesInitialResponse = chatService.sendMessage(request, user, true);
        ApiResponse<ChatMessagesInitialResponse> response = ApiResponse.<ChatMessagesInitialResponse>builder()
                .status(HttpStatus.OK.value())
                .message("Send message successfully")
                .data(chatMessagesInitialResponse)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }


    @GetMapping("/conversations")
    @Operation(summary = "Lấy danh sách hội thoại")
    public ResponseEntity<ApiResponse<Slice<ConversationResponse>>> getConversations(
            @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable,
            @AuthenticationPrincipal User user) {
        Slice<ConversationResponse> conversations = chatService.getUserConversations(user, pageable);
        ApiResponse<Slice<ConversationResponse>> response = ApiResponse.<Slice<ConversationResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get conversations successfully")
                .data(conversations)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/conversation/{conversationId}/messages")
    @Operation(summary = "Lấy danh sách tin nhắn trong hội thoại")
    public ResponseEntity<ApiResponse<Slice<ChatMessageResponse>>> getMethodName(
            @PathVariable Long conversationId,
            @ParameterObject @PageableDefault(page = 1, size = 10) Pageable pageable,
            @AuthenticationPrincipal User user) {

        Slice<ChatMessageResponse> messages = chatService.getConversationMessages(conversationId, user, pageable);
        ApiResponse<Slice<ChatMessageResponse>> response = ApiResponse.<Slice<ChatMessageResponse>>builder()
                .status(HttpStatus.OK.value())
                .message("Get messages successfully")
                .data(messages)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("conversation/{conversationId}/read-all")
    public ResponseEntity<ApiResponse<Void>> markAsRead(@PathVariable Long conversationId,
                                                        @AuthenticationPrincipal User user
    ) {
        
        chatService.markAsRead(conversationId, user);
        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status(HttpStatus.OK.value())
                .message("Mark all read successfully")
                .data(null)
                .build();
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
    
}
