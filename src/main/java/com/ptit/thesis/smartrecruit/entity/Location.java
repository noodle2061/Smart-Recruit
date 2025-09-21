package com.ptit.thesis.smartrecruit.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location extends BaseEntity {

    @Column(unique = true, nullable = false)
    String provinceCity;

    @Column(length = 100)
    String commune;

    @Column(unique = true, nullable = false)
    String country;

    @Column(unique = true, nullable = false, length = 150)
    String slug;

    @OneToMany(mappedBy = "location")
    Set<CompanyLocation> companyLocations;

    @OneToMany(mappedBy = "location")
    Set<JobAlert> jobAlerts;

    @OneToMany(mappedBy = "location", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    Set<Job> jobs;
}
