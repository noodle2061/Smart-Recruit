package com.ptit.thesis.smartrecruit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class S3Config {
    
    S3Properties s3Properties;

    @Bean
    public S3Client S3Client() {
        AwsBasicCredentials awCredentials = AwsBasicCredentials.create(s3Properties.getAccessKeyId(), s3Properties.getSecretAccessKey());
        String s3Region = s3Properties.getRegion();
        return S3Client.builder()
                .region(Region.of(s3Region))
                .credentialsProvider(StaticCredentialsProvider.create(awCredentials))
                .build();
    }

    @Bean
    public S3Presigner s3Presigner() {
        AwsBasicCredentials awsBasicCredentials = AwsBasicCredentials.create(s3Properties.getAccessKeyId(), s3Properties.getSecretAccessKey());
        String s3Region = s3Properties.getRegion();
        return S3Presigner.builder()
        .region(Region.of(s3Region))
        .credentialsProvider(StaticCredentialsProvider.create(awsBasicCredentials))        
        .build();
    }
}
