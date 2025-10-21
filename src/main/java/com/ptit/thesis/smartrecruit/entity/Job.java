package com.ptit.thesis.smartrecruit.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;

import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;
import com.ptit.thesis.smartrecruit.enums.JobStatus;
import com.ptit.thesis.smartrecruit.enums.JobType;
import com.ptit.thesis.smartrecruit.enums.SalaryType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

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
    JobType type;

    @Column(nullable = false)
    Integer vacancies;

    @Column(name = "expiration_date", nullable = false)
    LocalDate expirationDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    JobStatus status;

    @Column(nullable = false)
    String slug;

    @Column(name = "is_featured", columnDefinition = "BOOLEAN DEFAULT false")
    Boolean isFeatured;

    LocalDateTime postedAt;

    LocalDateTime deleteAt;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Application> jobApplications;

    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<SavedJob> savedJobs;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "job_tags",
                joinColumns = @JoinColumn(name = "job_id"),
                inverseJoinColumns = @JoinColumn(name = "tag_id"))
    Set<Tag> tags;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "job_jobcategories",
                joinColumns = @JoinColumn(name = "job_id"),
                inverseJoinColumns = @JoinColumn(name = "category_id"))
    Set<JobCategory> jobCategories;

    @PrePersist
    public void prePersist() {
        if (vacancies == null) {
            vacancies = 1;
        }
        
        if (status == null) {
            status = JobStatus.ACTIVE;
        }

        if (isFeatured == null) {
            isFeatured = false;
        }

        if (postedAt == null) {
            postedAt = LocalDateTime.now();
        }
    }
}
