package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.JobAlertFrequency;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "job_alert", 
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "keywords", "location_id", "category_id"})
)
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobAlert extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    String keywords;

    @ManyToOne
    @JoinColumn(name = "location_id")
    Location location;

    @ManyToOne
    @JoinColumn(name = "category_id")
    JobCategory jobCategory;

    @Enumerated(EnumType.STRING)
    @Column(name = "frequency", nullable = false)
    JobAlertFrequency frequency;
}
