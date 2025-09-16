package com.ptit.thesis.smartrecruit.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "page")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Page extends BaseEntity {

    @Column(nullable = false, unique = true, length = 150)
    String slug;

    @Column(nullable = false)
    String title;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    String content;
}
