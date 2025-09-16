package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "subscription")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Subscription extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    PricingPlan pricingPlan;

    @Column(name = "start_date", nullable = false)
    LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    SubscriptionStatus status;

    @OneToMany(mappedBy = "subscription", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Set<Invoice> invoices;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = SubscriptionStatus.ACTIVE;
        }

        if (startDate == null) {
            startDate = LocalDate.now();
        }
    }
}
