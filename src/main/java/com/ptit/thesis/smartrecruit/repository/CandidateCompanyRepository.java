package com.ptit.thesis.smartrecruit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.CandidateCompany;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Company;

@Repository
public interface CandidateCompanyRepository extends JpaRepository<CandidateCompany, Long> {
    Optional<CandidateCompany> findByCandidateAndCompany(CandidateProfile candidate, Company company);
}
