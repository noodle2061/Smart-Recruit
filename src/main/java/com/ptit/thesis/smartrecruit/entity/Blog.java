package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.BlogStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "blogs")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Blog extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User author;

    @Column(nullable = false)
    String title;

    @Column(nullable = true)
    String thumbnail;

    @Column(nullable = false, unique = true, length = 150)
    String slug;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    String content;

    String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    BlogStatus status;

    @Column(nullable = false)
    LocalDateTime publishedAt;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "blog_blogcategories",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "blogcategory_id"))
    Set<BlogCategory> categories;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "blog_tags",
            joinColumns = @JoinColumn(name = "blog_id"),
            inverseJoinColumns = @JoinColumn(name = "tag_id"))
    Set<Tag> tags;

    @PrePersist
    public void prePersist() {
        if (status == null) {
                status = BlogStatus.DRAFT;
        }

        if (publishedAt == null) {
                publishedAt = LocalDateTime.now();
        }
    }
}
