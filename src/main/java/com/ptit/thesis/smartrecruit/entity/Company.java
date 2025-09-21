package com.ptit.thesis.smartrecruit.entity;

import com.ptit.thesis.smartrecruit.enums.CompanyTeamSize;
import com.ptit.thesis.smartrecruit.enums.IndustryType;
import com.ptit.thesis.smartrecruit.enums.OrganizationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Table(name = "company")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Company extends BaseEntity{

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(nullable = false, unique = true, length = 255)
    String companyName;

    @Column(length = 512)
    String logoUrl;

    @Column(length = 512)
    String coverPhotoUrl;

    @Lob
    @Column(nullable = false, columnDefinition = "TEXT")
    String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrganizationType organizationType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    IndustryType industryType;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    CompanyTeamSize teamSize;

    Integer foundedIn;

    @Column(length = 512)
    String website;

    @Lob
    @Column(columnDefinition = "TEXT")
    String companyVision;

    @Lob
    @Column(columnDefinition = "TEXT")
    String companyBenefits;

    @Column(nullable = false,unique = true, length = 20)
    String phone;

    @Column(nullable = false,unique = true, length = 255)
    String email;

    @Column(nullable = false)
    Boolean isDeleted;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<CompanyLocation> locations;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<FollowedCompany> followedCompanies;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Job> jobs;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<ApplicationStatusColumn> applicationStatusColumns;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Subscription> subscriptions;

    @PrePersist
    public void prePersist() {
        if (isDeleted == null) {
            isDeleted = false;
        }
    }
}
