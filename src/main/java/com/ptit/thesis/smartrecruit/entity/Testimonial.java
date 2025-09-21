package com.ptit.thesis.smartrecruit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "testimonial")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Testimonial extends BaseEntity {

    @Column(nullable = false, length = 100)
    String authorName;

    @Column(nullable = false, length = 100)
    String authorTitle;

    @Column(length = 512)
    String authorAvatarUrl;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    String content;
}
