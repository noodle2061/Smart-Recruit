package com.ptit.thesis.smartrecruit.service.impl;

import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.ptit.thesis.smartrecruit.dto.request.OAuthRegisterRequest;
import com.ptit.thesis.smartrecruit.dto.request.RegisterRequest;
import com.ptit.thesis.smartrecruit.dto.response.UserResponse;
import com.ptit.thesis.smartrecruit.entity.Role;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.entity.UserRole;
import com.ptit.thesis.smartrecruit.exception.InvalidFieldException;
import com.ptit.thesis.smartrecruit.exception.RegistrationException;
import com.ptit.thesis.smartrecruit.exception.ResourceNotFoundException;
import com.ptit.thesis.smartrecruit.mapper.UserMapper;
import com.ptit.thesis.smartrecruit.notification.NotificationService;
import com.ptit.thesis.smartrecruit.repository.RoleRepository;
import com.ptit.thesis.smartrecruit.repository.UserRepository;
import com.ptit.thesis.smartrecruit.security.FirebaseUtil;
import com.ptit.thesis.smartrecruit.service.AuthService;
import com.ptit.thesis.smartrecruit.utils.StringUtil;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    UserMapper userMapper;
    NotificationService notificationService;
    FirebaseUtil firebaseUtil;
    UserRepository userRepository;
    RoleRepository roleRepository;

    @Transactional
    @Override
    public UserResponse register(RegisterRequest request) {

        String roleUpper = request.getRole().toUpperCase();

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new InvalidFieldException("Email is already in use.");
        }

        if (userRepository.existsByUserName(request.getUserName())) {
            throw new InvalidFieldException("Username is already in use.");
        }

        log.info("Registering user with email: {}", request.getEmail());

        User newEntityUser = new User();
        String firebaseUid = null;

        UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                .setEmail(request.getEmail())
                .setEmailVerified(false)
                .setPassword(request.getPassword())
                .setDisplayName(request.getFullName())
                .setDisabled(false);

        try {
            // Tạo user trong Firebase Authentication
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(createRequest);
            log.info("Successfully created new user: {}", userRecord.getUid());
            firebaseUid = userRecord.getUid();

            // gửi mail xác thực
            String verificationLink = FirebaseAuth.getInstance().generateEmailVerificationLink(request.getEmail());
            notificationService.sendVerificationMessage(request.getEmail(), request.getFullName(), verificationLink);
        } catch (FirebaseAuthException e) {
            log.error("Error creating new user: {}", e.getMessage());
            throw new RegistrationException("Error creating new user from Firebase: " + e.getMessage());
        } catch (Exception e) {
            // Nếu gửi mail thất bại, xóa user đã tạo trong Firebase để tránh rác
            if (firebaseUid != null) {
                FirebaseAuth.getInstance().deleteUserAsync(firebaseUid);
            }

            log.error("Error creating new user (could not send verification email): {}", e.getMessage());
            throw new RegistrationException(
                    "Error creating new user (could not send verification email): " + e.getMessage());
        }
        newEntityUser.setEmail(request.getEmail());
        newEntityUser.setUserFirebaseUid(firebaseUid);
        newEntityUser.setFullName(request.getFullName());
        newEntityUser.setUserName(request.getUserName());
        newEntityUser.setDeleted(false);

        Role roleOfUser = roleRepository.findByRoleName(roleUpper)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.getRole()));

        UserRole userRole = new UserRole();
        userRole.setRole(roleOfUser);
        userRole.setUser(newEntityUser);
        newEntityUser.setUserRoles(Set.of(userRole));

        User savedUser = userRepository.save(newEntityUser);
        log.info("User saved to database successfully with ID: {}", savedUser.getId());

        UserResponse userResponse = userMapper.toUserResponse(newEntityUser);
        userResponse.setRole(roleOfUser.getRoleName());
        return userResponse;
    }

    @Transactional
    @Override
    public UserResponse processAuth2CallBack(String authorization, OAuthRegisterRequest request) {

        log.info("Processing OAuth2 callback for token.");

        String cleanToken = (authorization.startsWith("Bearer ")) ? authorization.substring(7) : authorization;
        FirebaseToken decodedToken = firebaseUtil.verifyToken(cleanToken);
        String firebaseUserUid = decodedToken.getUid();

        Optional<User> existingUser = userRepository.findByUserFirebaseUid(firebaseUserUid);

        if (existingUser.isPresent()) { // User đã tồn tại, là luồng đăng nhập trả về thông tin user

            log.info("User already exists with Firebase UID: {}, process login flow", firebaseUserUid);

            UserResponse userResponse = userMapper.toUserResponse(existingUser.get());
            String roleName = existingUser.get().getUserRoles().stream()
                    .findFirst()
                    .map(userRole -> userRole.getRole().getRoleName())
                    .orElse("");
            userResponse.setRole(roleName);

            log.info("OAuth login completed successfully for email: {}", existingUser.get().getEmail());

            return userResponse;
        } else { // User chưa tồn tại, là luồng đăng ký

            log.info("No user found with Firebase UID: {}, process registration flow", firebaseUserUid);

            String roleUpper = request.getRole().toUpperCase();
            User newEntityUser = new User();
            String email = decodedToken.getEmail();
            String userName = generateUniqueUsernameFromEmail(email);

            newEntityUser.setEmail(email);
            newEntityUser.setUserFirebaseUid(firebaseUserUid);
            newEntityUser.setFullName(decodedToken.getName());
            newEntityUser.setUserName(userName);
            newEntityUser.setDeleted(false);

            Role roleOfUser = roleRepository.findByRoleName(roleUpper)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.getRole()));

            UserRole userRole = new UserRole();
            userRole.setRole(roleOfUser);
            userRole.setUser(newEntityUser);

            newEntityUser.setUserRoles(Set.of(userRole));

            try {
                User savedUser = userRepository.save(newEntityUser);

                log.info("User saved to database successfully with ID: {}", savedUser.getId());

                UserResponse userResponse = userMapper.toUserResponse(newEntityUser);
                userResponse.setRole(roleOfUser.getRoleName());

                log.info("OAuth registration completed successfully for email: {}", email);

                return userResponse;
            } catch (Exception e) {
                log.error("Error during OAuth registration: {}", e.getMessage());
                // Xóa user đã tạo trong Firebase để tránh rác
                FirebaseAuth.getInstance().deleteUserAsync(firebaseUserUid);
                throw new RegistrationException("Error during OAuth registration: " + e.getMessage());
            }
        }
    }

    private String generateUniqueUsernameFromEmail(String email) {
        String userName = StringUtil.generateUsernameFromEmail(email);

        if (userRepository.existsByUserName(userName)) {
            Optional<Integer> maxSuffixOpt = userRepository.findUserNameMaxSuffix(userName);
            String newSuffixStr = maxSuffixOpt.map(suffix -> String.valueOf(suffix + 1)).orElse("1");
            int userNamePrefixLength = Math.min(30, userName.length()) - newSuffixStr.length();
            userName = userName.substring(0, userNamePrefixLength) + newSuffixStr;
        }

        return userName;
    }
}
