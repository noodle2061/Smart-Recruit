package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.JobApplicationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "applications",
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "job_id"})
)
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Application extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "job_id", nullable = false)
    Job job;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    CandidateProfile candidate;

    @ManyToOne
    @JoinColumn(name = "resume_id", nullable = false)
    Resume resume;

    @Lob
    @Column(columnDefinition = "TEXT")
    String coverLetter;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    JobApplicationStatus status;

    @ManyToOne
    @JoinColumn(name = "status_column_id")
    ApplicationStatusColumn statusColumn;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = JobApplicationStatus.SUBMITTED;
        }
    }
}
