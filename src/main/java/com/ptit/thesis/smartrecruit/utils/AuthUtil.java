package com.ptit.thesis.smartrecruit.utils;

import org.springframework.security.core.context.SecurityContextHolder;

import com.ptit.thesis.smartrecruit.entity.User;

public class AuthUtil {
    public static User getCurrentUser() {
        return (User) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
    }
}
