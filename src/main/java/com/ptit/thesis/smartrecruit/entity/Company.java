package com.ptit.thesis.smartrecruit.entity;

import java.time.LocalDateTime;
import java.util.Set;

import com.ptit.thesis.smartrecruit.enums.CompanyTeamSize;
import com.ptit.thesis.smartrecruit.enums.IndustryType;
import com.ptit.thesis.smartrecruit.enums.OrganizationType;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "companies")
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Company extends BaseEntity{

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(nullable = false, unique = true, length = 255)
    String name;

    @Column(length = 512)
    String logoUrl;

    @Column(length = 512)
    String bannerUrl;

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

    @Column(nullable = false,unique = true, length = 20)
    String phone;

    @Column(nullable = false,unique = true, length = 255)
    String email;

    @Column(nullable = false)
    LocalDateTime deleteAt;

    @ManyToOne
    @JoinColumn(name = "location_id", nullable = false)
    Location location;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<Job> jobs;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    Set<ApplicationStatusColumn> applicationStatusColumns;
}
