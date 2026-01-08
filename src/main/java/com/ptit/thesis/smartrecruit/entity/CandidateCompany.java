package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.FollowType;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "candidate_companies",
    uniqueConstraints = {
           @UniqueConstraint(columnNames = {"candidate_id", "company_id", "type"})
       })
public class CandidateCompany extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    CandidateProfile candidate;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    Company company;

    @Enumerated(EnumType.STRING)
    FollowType type;
}
