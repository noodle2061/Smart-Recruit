package com.ptit.thesis.smartrecruit.utils;

public class StringUtil {

    public static int MAX_USERNAME_PREFIX_LENGTH = 20;

    public static String generateUsernameFromEmail(String email) {
        if (email == null || !email.contains("@")) {
            throw new IllegalArgumentException("Invalid email address");
        }

        String username = email.replaceAll("[^a-zA-Z0-9._-]", "")
                                .substring(0, email.indexOf("@"));
        
        if (username.length() > MAX_USERNAME_PREFIX_LENGTH) {
            username = username.substring(0, MAX_USERNAME_PREFIX_LENGTH);
        }
        return username;
    }
}
