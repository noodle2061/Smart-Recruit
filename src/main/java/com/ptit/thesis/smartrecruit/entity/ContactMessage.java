package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.ContactMessageStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "contact_message")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactMessage extends BaseEntity {

    @Column(name = "name", nullable = false, length = 100)
    String name;

    @Column(name = "email", nullable = false, length = 100)
    String email;

    @Column(name = "subject", nullable = false)
    String subject;

    @Lob
    @Column(name = "message", nullable = false, columnDefinition = "TEXT")
    String message;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    ContactMessageStatus status;

    @PrePersist
    public void prePersist() {
        if (status == null) {
            status = ContactMessageStatus.NEW;
        }
    }
}
