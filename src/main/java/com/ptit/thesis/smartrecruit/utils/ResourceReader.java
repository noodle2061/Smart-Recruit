package com.ptit.thesis.smartrecruit.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ResourceReader {

    /**
     * Đọc email từ resource/templates/email/{fileName}.html
     * @param fileName
     * @return Nội dung file dưới dạng String
     */
    public static String readEmailTemplate(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            throw new IllegalArgumentException("File name must not be null or empty");
        }
        String filePath = "templates/email/" + (fileName.endsWith(".html") ? fileName : fileName + ".html");
        try (InputStream inputStream = ResourceReader.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                throw new IllegalArgumentException("File not found: " + filePath);
            }
            return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException("Error reading file: " + filePath, e);
        }
    }
}
