package com.ptit.thesis.smartrecruit.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.query.Param;

import com.ptit.thesis.smartrecruit.dto.response.CandidatePageResponse;
import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;
import com.ptit.thesis.smartrecruit.enums.Gender;

public interface CandidateProfileRepositoryCustom {
    Page<CandidatePageResponse> findCandidatesWithFilter(
        @Param("keyword") String keyword,
        @Param("location") String location,
        @Param("category") String category,
        @Param("experienceLevel") ExperienceLevel experienceLevel,
        @Param("educationLevels") List<EducationLevel> educationLevels,
        @Param("gender") Gender gender,
        @Param("companyId") Long companyId,
        Pageable pageable
    );

    Page<CandidatePageResponse> findSavedCandidatesForEmployer(
        Pageable pageable
    );
}
