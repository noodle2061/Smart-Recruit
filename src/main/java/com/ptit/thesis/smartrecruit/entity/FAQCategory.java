package com.ptit.thesis.smartrecruit.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "faq_category")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FAQCategory extends BaseEntity {

    @Column(unique = true, nullable = false, length = 100)
    String name;

    @Column(nullable = false)
    Integer displayOrder;

    @OneToMany(mappedBy = "faqCategory", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<FAQItem> faqItems;

    @PrePersist
    public void prePersist() {
        if (displayOrder == null) {
            displayOrder = 0;
        }
    }
}
