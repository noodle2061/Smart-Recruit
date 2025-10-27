package com.ptit.thesis.smartrecruit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.dto.common.CompanyBasicInfoDTO;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.entity.User;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {
    Optional<Company> findByUser(User user);
    boolean existsByName(String name);

    @Query("SELECT new com.ptit.thesis.smartrecruit.dto.common.CompanyBasicInfoDTO(" +
    "c.name, c.logoUrl, c.foundedIn, c.website, c.email, c.phone, c.teamSize)" +
    "FROM Company c WHERE c.user = :user")
    CompanyBasicInfoDTO findBasicInfoByUser(User user);

    boolean existsByUser(User user);
}
