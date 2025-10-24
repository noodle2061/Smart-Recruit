package com.ptit.thesis.smartrecruit.utils;

public class StringUtil {

    public static final Integer MAX_USERNAME_PREFIX_LENGTH = 20;

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

    // chuẩn hóa tên riêng
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    // chuẩn hóa tên riêng gồm nhiều tiếng
    public static String standardizeName(String name) {
        String[] words = name.split(" ");
        StringBuilder result = new StringBuilder();
        for (String word : words) {
            result.append(capitalizeFirstLetter(word)).append(" ");
        }
        return result.toString().trim();
    }

    public static String generateSlug(String name) {
        return name.strip().toLowerCase().replaceAll("\\s+", "-") + "-" + System.currentTimeMillis();
    }
}
