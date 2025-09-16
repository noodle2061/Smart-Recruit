package com.ptit.thesis.smartrecruit.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "app_user")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User extends BaseEntity{

    @Column(unique = true, nullable = false)
    String userFirebaseUid;

    @Column(nullable = false)
    String fullName;

    @Column(nullable = false, unique = true, length = 50)
    String userName;

    @Column(nullable = false, unique = true, length = 50)
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

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles;
}
