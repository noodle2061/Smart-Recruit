package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.JobApplicationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

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
    @JoinColumn(name = "user_id", nullable = false)
    User user;

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
