package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.PostStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "post")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Post extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User author;

    @Column(nullable = false)
    String title;

    @Column(nullable = false, unique = true, length = 150)
    String slug;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    String content;

    String excerpt;

    @Column(length = 512)
    String coverImageUrl;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    PostStatus status;

    @Column(nullable = false)
    LocalDateTime publishedAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    Set<PostCategory> postCategories;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<BlogComment> blogComments;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<PostTag> postTags;

    @PrePersist
    public void prePersist() {
        if (status == null) {
                status = PostStatus.DRAFT;
        }

        if (publishedAt == null) {
                publishedAt = LocalDateTime.now();
        }
    }
}
