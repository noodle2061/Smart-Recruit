package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.CommentableType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "comments")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Comment extends BaseEntity {

    @Column(nullable = false)
    Long commentableId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    CommentableType commentableType;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    Comment parent;

    @Lob
    @Column(columnDefinition = "TEXT", nullable = false)
    String content;
}
