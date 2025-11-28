package com.ptit.thesis.smartrecruit.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import com.ptit.thesis.smartrecruit.dto.request.OAuthRegisterRequest;
import com.ptit.thesis.smartrecruit.dto.request.RegisterRequest;
import com.ptit.thesis.smartrecruit.dto.response.UserResponse;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Role;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.exception.InvalidFieldException;
import com.ptit.thesis.smartrecruit.exception.RegistrationException;
import com.ptit.thesis.smartrecruit.exception.ResourceNotFoundException;
import com.ptit.thesis.smartrecruit.mapper.UserMapper;
import com.ptit.thesis.smartrecruit.repository.CandidateProfileRepository;
import com.ptit.thesis.smartrecruit.repository.CompanyRepository;
import com.ptit.thesis.smartrecruit.repository.RoleRepository;
import com.ptit.thesis.smartrecruit.repository.UserRepository;
import com.ptit.thesis.smartrecruit.security.FirebaseUtil;
import com.ptit.thesis.smartrecruit.service.AuthService;
import com.ptit.thesis.smartrecruit.service.NotificationService;
import com.ptit.thesis.smartrecruit.service.S3Service;
import com.ptit.thesis.smartrecruit.utils.Constant;
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

    FirebaseUtil firebaseUtil;
    UserMapper userMapper;
    NotificationService notificationService;
    UserRepository userRepository;
    RoleRepository roleRepository;
    CandidateProfileRepository candidateProfileRepository;
    CompanyRepository companyRepository;
    S3Service s3Service;

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
        String customToken = null;

        UserRecord.CreateRequest createRequest = new UserRecord.CreateRequest()
                .setEmail(request.getEmail())
                .setEmailVerified(false)
                .setPassword(request.getPassword())
                .setDisabled(false);

        try {
            // Tạo user trong Firebase Authentication
            UserRecord userRecord = FirebaseAuth.getInstance().createUser(createRequest);
            log.info("Successfully created new user: {}", userRecord.getUid());
            firebaseUid = userRecord.getUid();
            customToken = FirebaseAuth.getInstance().createCustomToken(firebaseUid);

            // gửi mail xác thực
            // Tạm thời bỏ đi, ủy quyền sang cho Front End xử lý
            // String verificationLink =
            // FirebaseAuth.getInstance().generateEmailVerificationLink(request.getEmail());
            // notificationService.sendVerificationMessage(request.getEmail(),
            // request.getFullName(), verificationLink);
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

        // lưu vào csdl
        newEntityUser.setEmail(request.getEmail());
        newEntityUser.setFirebaseUid(firebaseUid);
        newEntityUser.setUserName(request.getUserName());

        Role roleOfUser = roleRepository.findByName(roleUpper)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + request.getRole()));

        newEntityUser.setRole(roleOfUser);

        if (roleOfUser.getName().equals(Constant.CANDIDATE_ROLE)) {
            CandidateProfile newCandidateProfile = new CandidateProfile();
            newCandidateProfile.setUser(newEntityUser);
            newEntityUser.setCandidateProfile(newCandidateProfile);
        }

        User savedUser = userRepository.save(newEntityUser);
        log.info("User saved to database successfully with ID: {}", savedUser.getId());

        UserResponse userResponse = userMapper.toUserResponse(newEntityUser);
        userResponse.setFirebaseCustomToken(customToken); // gửi token để gửi email.
        return userResponse;
    }

    @Transactional
    @Override
    public UserResponse login(String authToken) {

        FirebaseToken firebaseToken = firebaseUtil.verifyToken(authToken);
        String uid = firebaseToken.getUid();
        User existingUser = userRepository.findByFirebaseUid(uid)
                .orElseThrow(() -> new ResourceNotFoundException("Not found user with uid: " + uid));

        UserResponse userResponse = toUserResponse(existingUser);

        log.info("Login completed successfully for email: {}", existingUser.getEmail());

        return userResponse;
    }

    @Override
    @Transactional
    public UserResponse processAuth2CallBack(String authorization, OAuthRegisterRequest request) {
        log.info("Processing OAuth2 callback for token.");

        String cleanToken = (authorization.startsWith("Bearer ")) ? authorization.substring(7) : authorization;
        FirebaseToken decodedToken = firebaseUtil.verifyToken(cleanToken);
        String firebaseUserUid = decodedToken.getUid();

        Optional<User> existingUserOpt = userRepository.findByFirebaseUid(firebaseUserUid);

        if (existingUserOpt.isPresent()) { // User đã tồn tại, là luồng đăng nhập trả về thông tin user

            log.info("User already exists with Firebase UID: {}, process login flow", firebaseUserUid);

            User existingUser = existingUserOpt.get();

            UserResponse userResponse = toUserResponse(existingUser);

            log.info("OAuth login completed successfully for email: {}", existingUser.getEmail());

            return userResponse;
        } else { // User chưa tồn tại, là luồng đăng ký, trong luồng này cần xem đã gửi request
                 // chưa, nếu chưa thì cần gửi lên request để xác nhận

            if (request == null || request.getRole() == null) { // phần này nhằm kiểm tra người dùng đã chọn role chưa, chưa có thì trả về exception để giao diện sẽ hiện role để chọn, mặc định ban đầu là không hiện role ra
                throw new InvalidFieldException("Role is required.");
            }

            log.info("No user found with Firebase UID: {}, process registration flow", firebaseUserUid);

            String roleUpper = request.getRole().toUpperCase();

            Role roleOfUser = roleRepository.findByName(roleUpper)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleUpper));

            User newEntityUser = new User();
            String email = decodedToken.getEmail();
            String userName = generateUniqueUsernameFromEmail(email);

            newEntityUser.setEmail(email);
            newEntityUser.setFirebaseUid(firebaseUserUid);
            newEntityUser.setUserName(userName);
            newEntityUser.setRole(roleOfUser);

            if (roleOfUser.getName().equals(Constant.CANDIDATE_ROLE)) {
                CandidateProfile newCandidateProfile = new CandidateProfile();
                newCandidateProfile.setUser(newEntityUser);
                newEntityUser.setCandidateProfile(newCandidateProfile);
            }

            try {
                User savedUser = userRepository.save(newEntityUser);

                log.info("User saved to database successfully with ID: {}", savedUser.getId());

                UserResponse userResponse = userMapper.toUserResponse(newEntityUser);
                userResponse.setRole(roleOfUser.getName());

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

        log.info("Generate unique name form {}", userName);

        if (userRepository.existsByUserName(userName)) {
            Optional<Integer> maxSuffixOpt = userRepository.findUserNameMaxSuffix(userName);
            log.info("maxSuffixOpt: {}", maxSuffixOpt);
            String newSuffixStr = maxSuffixOpt.map(suffix -> String.valueOf(suffix + 1)).orElse("1");
            int userNamePrefixLength = Math.min(30, userName.length()) - newSuffixStr.length() + 1;
            userName = userName.substring(0, userNamePrefixLength) + newSuffixStr;
        }

        log.info("Finish generate unique name, name: {}", userName);

        return userName;
    }


    public UserResponse toUserResponse(User savedUser) {
        UserResponse response = userMapper.toUserResponse(savedUser);
        String roleString = savedUser.getRole().getName();
        if (roleString.equals(Constant.CANDIDATE_ROLE)) {
            response.setFullName(candidateProfileRepository.findFullNameByUser(savedUser));
        } else if (roleString.equals(Constant.EMPLOYER_ROLE)) {
            response.setAvatar(s3Service.generatePresignedUrl(candidateProfileRepository.findAvatarByUser(savedUser)));
        } else if (roleString.equals(Constant.EMPLOYER_ROLE)) {
            response.setCompanySetup(companyRepository.existsByUser(savedUser));
            response.setAvatar(companyRepository.findAvatarByUser(savedUser));
        }
        return response;
    }
}
