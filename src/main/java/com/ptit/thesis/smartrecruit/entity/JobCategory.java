package com.ptit.thesis.smartrecruit.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "job_category")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class JobCategory extends BaseEntity {

    @Column(unique = true, nullable = false, length = 100)
    String name;

    @Column(nullable = false, unique = true, length = 150)
    String slug;

    @Column(length = 512)
    String iconUrl;

    @OneToMany(mappedBy = "jobCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<JobToCategory> jobToCategories;

    @OneToMany(mappedBy = "jobCategory", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    Set<JobAlert> jobAlerts;
}
