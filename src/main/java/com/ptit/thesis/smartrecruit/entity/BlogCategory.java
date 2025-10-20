package com.ptit.thesis.smartrecruit.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "blog_categories")
@Setter
@Getter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogCategory extends BaseEntity {

    @Column(nullable = false, unique = true, length = 100)
    String name;

    @OneToMany(mappedBy = "blogCategory", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PostCategory> postCategories;
}
