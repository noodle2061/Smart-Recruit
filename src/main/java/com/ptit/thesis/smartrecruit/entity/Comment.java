package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.CommentableType;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(callSuper = true)
public class Comment extends BaseEntity {

    @Column(nullable = false)
    Long commentableId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    CommentableType commentableType;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    String content;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    Comment parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true)
    List<Comment> children = new ArrayList<>();

}
