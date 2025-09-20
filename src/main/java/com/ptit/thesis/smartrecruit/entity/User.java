package com.ptit.thesis.smartrecruit.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity implements UserDetails {

    @Column(unique = true, nullable = false)
    @NotBlank(message = "Firebase UID is mandatory")
    String userFirebaseUid;

    @Column(nullable = false)
    @NotBlank(message = "Full name is mandatory")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    String fullName;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "Username is mandatory")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    String userName;

    @Column(nullable = false, unique = true, length = 50)
    @NotBlank(message = "Email is mandatory")
    @Email(message = "Email should be valid")
    String email;

    @Column(nullable = false)
    boolean isDeleted;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    CandidateProfile candidateProfile;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Resume> resumes;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    Set<Company> companies;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<FollowedCompany> followedCompanies;

    @OneToMany(mappedBy = "employerUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<SavedCandidate> savedAsEmployer;

    @OneToMany(mappedBy = "candidateUser", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<SavedCandidate> savedAsCandidate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<JobApplication> jobApplications;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<SavedJob> savedJobs;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<JobAlert> jobAlerts;

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Post> posts;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<BlogComment> blogComments;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<UserRole> userRoles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (userRoles == null || userRoles.isEmpty()) {
            return Collections.emptyList();
        }
        return userRoles.stream()
                .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getRoleName().toUpperCase()))
                .collect(Collectors.toSet());
    }

    /**
     * Vì ứng dụng sử dụng Firebase để xác thực, mật khẩu không được lưu trữ trong CSDL.
     * Do đó, phương thức này có thể trả về null hoặc ném ra một exception.
     *
     * @return null
     */
    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.userName;
    }
}
