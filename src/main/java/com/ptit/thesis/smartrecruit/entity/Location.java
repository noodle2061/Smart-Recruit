package com.ptit.thesis.smartrecruit.entity;

import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Table(name = "locations",
        uniqueConstraints = @UniqueConstraint(columnNames = {"latitude", "longitude"}))
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Location extends BaseEntity {

    @Column(nullable = false)
    Float latitude;

    @Column(nullable = false)
    Float longitude;

    @Column(unique = true, nullable = false)
    String country;

    @Column(unique = true, nullable = false)
    String provinceCity;

    @Column(length = 100)
    String commune;

    @OneToMany(mappedBy = "location")
    Set<Company> companies;

    @OneToMany(mappedBy = "location", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    Set<Job> jobs;

    @OneToMany(mappedBy = "location", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    Set<CandidateProfile> candidateProfiles;
}
