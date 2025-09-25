package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Entity
@Table(name = "candidate_profile")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CandidateProfile extends BaseEntity{

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    User user;

    @Column(length = 512)
    String profilePictureUrl;

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

    @Column(nullable = false)
    Boolean isPublic;

    @PrePersist
    public void prePersist() {
        if (isPublic == null) {
            isPublic = true;
        }
    }
}
