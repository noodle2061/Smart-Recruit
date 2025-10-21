package com.ptit.thesis.smartrecruit.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Table(name = "tags")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Tag {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    String name;

    @ManyToMany(mappedBy = "tags")
    Set<Job> jobs;

    @ManyToMany(mappedBy = "tags")
    Set<Blog> blogs;
}
