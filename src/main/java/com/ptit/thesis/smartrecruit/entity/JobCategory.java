package com.ptit.thesis.smartrecruit.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "job_categories")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobCategory extends BaseEntity {

    @Column(unique = true, nullable = false, length = 100)
    String name;

    @ManyToMany(mappedBy = "jobCategories")
    Set<Job> jobs;
}
