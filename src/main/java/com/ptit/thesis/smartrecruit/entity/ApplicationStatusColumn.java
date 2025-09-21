package com.ptit.thesis.smartrecruit.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "application_status_column")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApplicationStatusColumn extends BaseEntity{

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    Company company;

    @Column(nullable = false, length = 100)
    String columnName;

    @Column(nullable = false)
    int columnOrder;

    @OneToMany(mappedBy = "statusColumn", fetch = FetchType.LAZY)
    Set<JobApplication> jobApplications;
}
