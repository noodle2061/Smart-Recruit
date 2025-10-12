package com.ptit.thesis.smartrecruit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@Component
@ConfigurationProperties(prefix = "aws.s3")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class S3Properties {
    String region;
    String bucketName;
    String accessKeyId;
    String secretAccessKey;
}
