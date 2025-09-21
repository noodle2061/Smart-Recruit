package com.ptit.thesis.smartrecruit.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "faq_item")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class FAQItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    FAQCategory faqCategory;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    String question;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    String answer;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    Integer displayOrder;

    @PrePersist
    public void prePersist() {
        if (displayOrder == null) {
            displayOrder = 0;
        }
    }
}
