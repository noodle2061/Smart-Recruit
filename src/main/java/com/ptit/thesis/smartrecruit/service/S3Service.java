package com.ptit.thesis.smartrecruit.service;

import java.io.IOException;
import java.time.Duration;

import org.springframework.web.multipart.MultipartFile;

public interface S3Service {
    public String uploadFile(MultipartFile file, String folderPath) throws IOException;
    public String generatePresignedUrl(String key);
    public String generatePresignedUrl(String key, Duration duration);
    public void deleteFileByKey(String key);
    public void deleteFileByUrl(String url);
}
