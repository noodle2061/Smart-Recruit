package com.ptit.thesis.smartrecruit.config;

import java.util.Set;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserRecord;
import com.ptit.thesis.smartrecruit.entity.Role;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.entity.UserRole;
import com.ptit.thesis.smartrecruit.repository.RoleRepository;
import com.ptit.thesis.smartrecruit.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@RequiredArgsConstructor
public class AdminAccountInitializer implements CommandLineRunner {
    AdminAccountProperties adminAccountProperties;
    RoleRepository roleRepository;
    UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (adminAccountProperties.isEnabled() == false) {
            log.info("Admin account creation is disabled.");
            return;
        }

        if (userRepository.existsByEmail(adminAccountProperties.getEmail())) {
            log.info("Admin account already exists. Skipping creation.");
            return;
        }

        // Tạo tài khoản admin
        log.info("Creating admin account with email: " + adminAccountProperties.getEmail());
        // Tạo tài khoản admin với các thuộc tính từ adminAccountProperties
        Role adminRole = roleRepository.findByRoleName("ADMIN")
                .orElseThrow(() -> new RuntimeException("Admin role not found in the database."));

        try {
            // Tạo tài khoản admin với các thuộc tính từ adminAccountProperties
            // Lưu tài khoản admin vào firebase
            // Tao nguoi dung trong firebase
            UserRecord.CreateRequest request = new UserRecord.CreateRequest()
                    .setEmail(adminAccountProperties.getEmail())
                    .setEmailVerified(true)
                    .setPassword(adminAccountProperties.getPassword())
                    .setDisplayName(adminAccountProperties.getFullName())
                    .setDisabled(false);
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(request);
            log.info("Successfully created new user in Firebase: " + userRecord.getUid());

            // Tạo entity User tương ứng và gán vai trò ADMIN
            User adminUser = new User();
            adminUser.setEmail(adminAccountProperties.getEmail());
            adminUser.setFullName(adminAccountProperties.getFullName());
            adminUser.setUserFirebaseUid(userRecord.getUid());
            adminUser.setUserName(adminAccountProperties.getUsername());
            adminUser.setDeleted(false);

            UserRole adminUserRole = new UserRole();
            adminUserRole.setRole(adminRole);
            adminUserRole.setUser(adminUser);

            adminUser.setUserRoles(Set.of(adminUserRole));

            // Lưu tài khoản admin vào database
            log.info("Admin account created successfully.");
            userRepository.save(adminUser);
        } catch (Exception e) {
            log.error("Failed to create admin account: " + e.getMessage());
            return;
        }

    }

}
