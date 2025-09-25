package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.LinkableType;
import com.ptit.thesis.smartrecruit.enums.PlatformName;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "social_link", indexes = {
        @Index(name = "idx_social_link_polymorphic", columnList = "linkable_type, linkable_id")
})
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SocialLink extends BaseEntity {

    @Column(nullable = false)
    Long linkableId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    LinkableType linkableType;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    PlatformName platformName;

    @Column(nullable = false, length = 512)
    String url;
}
