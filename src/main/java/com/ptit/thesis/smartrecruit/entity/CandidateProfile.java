package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.*;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "candidate_profiles")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateProfile extends BaseEntity{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    User user;

    @Column(nullable = false)
    @NotBlank(message = "Full name is mandatory")
    @Size(min = 2, max = 100, message = "Full name must be between 2 and 100 characters")
    String fullName;

    @Column(length = 512)
    String avatarUrl;

    @Column(nullable = false, length = 255)
    String headline;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    ExperienceLevel experienceLevel;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    EducationLevel educationLevel;

    @Column(length = 512)
    String personalWebsite;

    @Enumerated(EnumType.STRING)
    Nationality nationality;

    LocalDate dateOfBirth;

    @Enumerated(EnumType.STRING)
    Gender gender;

    @Enumerated(EnumType.STRING)
    MaritalStatus maritalStatus;

    @Column(length = 255)
    String biography;

    @Column(unique = true, length = 20)
    String phone;

    @Column(nullable = false)
    Boolean isPublic;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Application> applications;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "location_id", nullable = false)
    Location location;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<SavedJob> savedJobs;

    @PrePersist
    public void prePersist() {
        if (isPublic == null) {
            isPublic = true;
        }
    }
}
