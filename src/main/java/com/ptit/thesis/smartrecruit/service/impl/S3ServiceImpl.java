package com.ptit.thesis.smartrecruit.service.impl;

import java.io.IOException;
import java.net.URL;
import java.time.Duration;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.ptit.thesis.smartrecruit.config.S3Properties;
import com.ptit.thesis.smartrecruit.exception.S3ErrorException;
import com.ptit.thesis.smartrecruit.service.S3Service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class S3ServiceImpl implements S3Service {

    S3Properties s3Properties;
    S3Client s3Client;
    S3Presigner s3Presigner;

    @Override
    public String uploadFile(MultipartFile file, String folderPath) throws IOException {

        log.info("Start to upload file to S3.");

        if (file.isEmpty()) {
            log.warn("Can not upload file because file is null or blank!");
            throw new IllegalStateException("Can not upload file because file is empty");
        }

        String originName = file.getOriginalFilename();
        String extensionName = "";

        if (originName != null && originName.contains(".")) {
            extensionName = originName.substring(originName.lastIndexOf("."));
        }

        String newRandomName = UUID.randomUUID() + "." + extensionName;
        String key = folderPath + "/" + newRandomName;

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(key)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            log.info("File upload successfully to S3");
        } catch (S3Exception e) {
            log.error("Fail to upload file to S3: {}", e.getMessage());
            throw new S3ErrorException("Fail to upload file to S3: " + e.getMessage());
        }

        return key;
    }

    @Override
    public String generatePresignedUrl(String key) {
        return generatePresignedUrl(key, Duration.ofMinutes(15));
    }

    @Override
    public String generatePresignedUrl(String key, Duration duration) {
        if (key == null || key.isBlank()) {
            return null;
        }

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(s3Properties.getBucketName())
                    .key(key)
                    .build();
            
            GetObjectPresignRequest getObjectPresignRequest = GetObjectPresignRequest.builder()
                                            .signatureDuration(duration)
                                            .getObjectRequest(getObjectRequest)
                                            .build();
            
            PresignedGetObjectRequest presignedGetObjectRequest = s3Presigner.presignGetObject(getObjectPresignRequest);

            String url = presignedGetObjectRequest.url().toString();

            log.info("Generate successfully pre-signed Url for key {}: {}", key, url);

            return url;
        } catch (S3Exception e) {
            log.error("Fail to generate pre-signed Url: {}", e.getMessage());
            throw new S3ErrorException("Fail to generate pre-signed Url: " + e.getMessage());
        }
    }

    @Override
    public void deleteFileByKey(String key) {
        if (key == null || key.isEmpty()) {
            log.warn("Can not delete file because key is null or blank!");
            return;
        }

        try {
            DeleteObjectRequest deleteObjectRequest = DeleteObjectRequest.builder()
                                    .bucket(s3Properties.getBucketName())
                                    .key(key)
                                    .build();
            
            s3Client.deleteObject(deleteObjectRequest);

            log.info("Delete successfully object with key {} from S3", key);
        } catch (S3Exception e) {
            throw new S3ErrorException("Fail to delete file with key " + key + " form S3: " + e.getMessage());
        }
    }

    @Override
    public void deleteFileByUrl(String fileUrl) {
         if (fileUrl == null || fileUrl.isBlank()) {
            log.warn("File URL is null or blank, skipping deletion.");
            return;
        }
        try {
            String key = getKeyFromUrl(fileUrl);
            deleteFileByKey(key);
        } catch (Exception e) {
            log.error("An unexpected error occurred while trying to delete file from S3 [{}]: {}", fileUrl, e.getMessage());
            throw new S3ErrorException("An unexpected error occurred during S3 file deletion: " + e);
        }
    }

    public String getKeyFromUrl(String fileUrl) {
        try {
            URL url = new URL(fileUrl);
            return url.getPath().substring(1);
        } catch (Exception e) {
            log.error("Invalid S3 URL format: {}", fileUrl);
            throw new S3ErrorException("Invalid S3 URL format");
        }
    }
}
