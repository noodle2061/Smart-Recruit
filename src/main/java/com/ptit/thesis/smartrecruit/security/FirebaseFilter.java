package com.ptit.thesis.smartrecruit.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.firebase.auth.FirebaseToken;
import com.ptit.thesis.smartrecruit.config.CustomUserDetailsService;
import com.ptit.thesis.smartrecruit.exception.InvalidTokenException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FirebaseFilter extends OncePerRequestFilter {
    FirebaseUtil firebaseUtil;
    CustomUserDetailsService customUserDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        final String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String clearTokenId = authorizationHeader.substring(7);

        try {
            FirebaseToken decodedToken = firebaseUtil.verifyToken(clearTokenId);

            // Kiem tra email da duoc xac thuc chua
            if (decodedToken.isEmailVerified() == false) {
                throw new InvalidTokenException("Email not verified.");
            }

            String userUid = decodedToken.getUid();

            UserDetails userDetails = customUserDetailsService.loadUserByUsername(userUid);

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (UsernameNotFoundException e) {
            throw new InvalidTokenException("User associated with token not found.");
        }
        
        filterChain.doFilter(request, response);
    }

    
}
