package com.ptit.thesis.smartrecruit.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.http.HttpStatus;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.ptit.thesis.smartrecruit.config.CustomUserDetailsService;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.exception.InvalidTokenException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class FirebaseFilter extends OncePerRequestFilter {

    FirebaseUtil firebaseUtil;
    CustomUserDetailsService customUserDetailsService;

    private final List<String> publicEndpoints = Arrays.asList(
            "/api/auth/register",
            "/api/auth/callback",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/api-docs/**");

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

            if (!userDetails.isAccountNonLocked()) {
                throw new LockedException("This account is locked. Please contact your administrator.");
            }

            // Dang nhap thanh cong, luu thong tin vao security context
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        } catch (UsernameNotFoundException e) {
            log.error("Error in FirebaseFilter: {}", e.getMessage());
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.getWriter().write("User is not registered in the System!");
            return;
        } catch (Exception e) {
            log.error("Error in FirebaseFilter: {}", e.getMessage());
            response.setStatus(HttpStatus.SC_UNAUTHORIZED);
            response.getWriter().write("The token is invalid or expire!");
            return;
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getRequestURI();
        AntPathMatcher pathMatcher = new AntPathMatcher();
        return publicEndpoints.stream().anyMatch(p -> pathMatcher.match(p, path));
    }

}
