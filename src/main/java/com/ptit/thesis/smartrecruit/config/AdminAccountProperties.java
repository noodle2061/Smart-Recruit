package com.ptit.thesis.smartrecruit.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * Gán cấu hình từ application.yml cho các thuộc tính của lớp này
 * để sử dụng trong việc khởi tạo tài khoản admin khi ứng dụng khởi động.
 */
@Data
@Component
@ConfigurationProperties(prefix = "admin.creation")
@FieldDefaults(level = lombok.AccessLevel.PRIVATE)
public class AdminAccountProperties {
    boolean enabled;
    String email;
    String password;
    String fullName;
    String username;
}
