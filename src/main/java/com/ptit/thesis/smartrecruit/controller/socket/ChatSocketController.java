package com.ptit.thesis.smartrecruit.controller.socket;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Controller;

import com.ptit.thesis.smartrecruit.dto.request.ChatMessageRequest;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.service.ChatService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Controller
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ChatSocketController {

    ChatService chatService;

    @MessageMapping("/chat")
    public void chat(
            @Payload ChatMessageRequest request,
            Principal principal) {
        User sender = null;
        if (principal instanceof UsernamePasswordAuthenticationToken) {
            sender = (User)((UsernamePasswordAuthenticationToken) principal).getPrincipal();
        }

        if (sender != null) {
            chatService.sendMessage(request, sender);
        } else {
            throw new IllegalArgumentException("User not authenticated");
        }
    }
}
