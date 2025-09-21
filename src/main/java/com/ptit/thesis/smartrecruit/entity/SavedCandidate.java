    package com.ptit.thesis.smartrecruit.entity;

    import jakarta.persistence.*;
    import lombok.*;
    import lombok.experimental.FieldDefaults;

    @Entity
    @Table(name = "saved_candidate",
            uniqueConstraints = @UniqueConstraint(columnNames = {"employer_user_id", "candidate_user_id"})
    )
    @Getter
    @Setter
    @NoArgsConstructor
    @FieldDefaults(level = AccessLevel.PRIVATE)
    public class SavedCandidate extends BaseEntity {

        @ManyToOne
        @JoinColumn(name = "employer_user_id", nullable = false)
        User employerUser;

        @ManyToOne
        @JoinColumn(name = "candidate_user_id", nullable = false)
        User candidateUser;
    }
