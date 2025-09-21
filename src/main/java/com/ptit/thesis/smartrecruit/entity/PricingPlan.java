package com.ptit.thesis.smartrecruit.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Set;

@Entity
@Table(name = "pricing_plan")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PricingPlan extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    String name;

    @Column(nullable = false, precision = 10, scale = 2)
    BigDecimal price;

    @Column(nullable = false)
    Integer durationDays;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    String features;

    @OneToMany(mappedBy = "pricingPlan", fetch = FetchType.LAZY)
    Set<Subscription> subscriptions;
}
