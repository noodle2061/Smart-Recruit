package com.ptit.thesis.smartrecruit.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "job_jobcategory",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"job_id", "category_id"})
        }
)
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobToCategory extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "job_id", nullable = false)
    Job job;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    JobCategory jobCategory;
}