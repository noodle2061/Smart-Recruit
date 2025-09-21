package com.ptit.thesis.smartrecruit.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "blog_tag")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BlogTag extends BaseEntity {

    @Column(nullable = false, length = 20)
    String name;

    @Column(nullable = false, unique = true, length = 20)
    String slug;

    @OneToMany(mappedBy = "blogTag", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTag> postTags;
}
