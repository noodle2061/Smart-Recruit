package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.*;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "jobs")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Job extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    Company company;

    @Column(nullable = false)
    String title;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    String description;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    String responsibilities;

    String tags;

    @Column(precision = 10, scale = 2, nullable = false)
    BigDecimal minSalary;

    @Column(precision = 10, scale = 2, nullable = false)
    BigDecimal maxSalary;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    SalaryType salaryType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    Location location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    EducationLevel educationLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ExperienceLevel experienceLevel;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    JobType jobType;

    @Column(nullable = false)
    Integer vacancies;

    @Column(name = "expiration_date", nullable = false)
    LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    JobStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    JobApplyOn applyOn;

    @Column(nullable = false)
    String slug;

    @Column(nullable = false)
    Boolean isDeleted;

    @Column(nullable = false)
    LocalDateTime postedAt;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<JobToCategory> jobToCategories;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Application> jobApplications;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<SavedJob> savedJobs;

    @PrePersist
    public void prePersist() {
        if (vacancies == null) {
            vacancies = 1;
        }
        
        if (status == null) {
            status = JobStatus.ACTIVE;
        }

        if (isDeleted == null) {
            isDeleted = false;
        }

        if (postedAt == null) {
            postedAt = LocalDateTime.now();
        }
    }
}
