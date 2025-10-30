package com.ptit.thesis.smartrecruit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.CandidateCompany;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.enums.FollowType;

@Repository
public interface CandidateCompanyRepository extends JpaRepository<CandidateCompany, Long> {
    Optional<CandidateCompany> findByCandidateAndCompany(CandidateProfile candidate, Company company);

    @Query("SELECT c FROM CandidateCompany c WHERE c.company.id = :companyId AND c.candidate.id = :candidateId AND c.type = :type")
    Optional<CandidateCompany> findByIdAndType(Long companyId, Long candidateId, FollowType type);
}
