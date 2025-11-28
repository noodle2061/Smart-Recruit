package com.ptit.thesis.smartrecruit.entity;

import jakarta.persistence.Entity;
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
@Table(name = "conversations",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"candidate_id", "company_id"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Conversation extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    CandidateProfile candidate;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    Company company;

    String lastMessageContent;
}
