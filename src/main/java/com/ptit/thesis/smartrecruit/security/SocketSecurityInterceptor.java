package com.ptit.thesis.smartrecruit.security;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.google.firebase.auth.FirebaseToken;
import com.ptit.thesis.smartrecruit.config.CustomUserDetailsService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class SocketSecurityInterceptor implements ChannelInterceptor {

    private final FirebaseUtil firebaseUtil;
    private final CustomUserDetailsService userDetailsService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authorizationHeader = accessor.getFirstNativeHeader("Authorization");
            
            if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
                String token = authorizationHeader.substring(7);
                try {
                    FirebaseToken decodedToken = firebaseUtil.verifyToken(token);
                    String firebaseUid = decodedToken.getUid();

                    UserDetails userDetails = userDetailsService.loadUserByUsername(firebaseUid);

                    UsernamePasswordAuthenticationToken authentication = 
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

                    accessor.setUser(authentication);
                    
                    log.info("User {} connected to WebSocket", userDetails.getUsername());
                } catch (Exception e) {
                    log.error("WebSocket Authentication failed: {}", e.getMessage());
                }
            } else {
                log.warn("WebSocket connection attempt without valid Authorization header");
            }
        }
        return message;
    }
}