package com.ptit.thesis.smartrecruit.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.dto.response.CandidatePageResponse;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.EducationLevel;
import com.ptit.thesis.smartrecruit.enums.ExperienceLevel;
import com.ptit.thesis.smartrecruit.enums.Gender;
import com.ptit.thesis.smartrecruit.entity.SavedJob;
import java.util.Set;


@Repository
public interface CandidateProfileRepository extends JpaRepository<CandidateProfile, Long>, CandidateProfileRepositoryCustom {
    Optional<CandidateProfile> findByUser(User user);

    // @Query(value = "SELECT new com.ptit.thesis.smartrecruit.dto.response.CandidatePageResponse(" + 
    // "c.id, c.fullName, c.avatarUrl, c.headline, c.experienceLevel, c.educationLevel, " + 
    // "new com.ptit.thesis.smartrecruit.dto.common.LocationDTO(l.country, l.provinceCity, l.commune, l.latitude, l.longitude), " +
    // "(EXISTS(SELECT 1 FROM CandidateCompany cc " + 
    // "                       WHERE cc.company.id = :companyId AND cc.candidate.id = c.id " + 
    // "                       AND cc.type = com.ptit.thesis.smartrecruit.enums.FollowType.COMPANY_FOLLOW_CANDIDATE))" + 
    // ") " + 
    // "FROM CandidateProfile c " +
    // "JOIN c.user u " + 
    // "LEFT JOIN c.location l " +
    // "WHERE " +
    // "(:keyword IS NULL OR " + 
    // "       c.fullName LIKE CONCAT('%', :keyword, '%') " + 
    // "       OR u.email LIKE CONCAT('%', :keyword, '%') " +
    // "       OR c.headline LIKE CONCAT('%', :keyword, '%') " + 
    // ") " + // keyword
    // "AND (:location IS NULL OR l.provinceCity LIKE CONCAT('%', :location, '%')) " + // location
    // // " AND (:category IS NULL)" + // category, tạm thời bỏ qua sau này sẽ thêm lại
    // "AND (:#{#experienceLevel} IS NULL OR c.experienceLevel = :#{#experienceLevel}) " + // experienceLevel
    // "AND ((:#{#educationLevels == null || #educationLevels.isEmpty()}) = true OR c.educationLevel IN :#{#educationLevels}) " + // educationLevels
    // "AND (:#{#gender} IS NULL OR c.gender = :#{#gender}) " + // gender
    // "AND (c.isPublic = true)", // public
    //     countQuery = "SELECT count(c) " + 
    //             "FROM CandidateProfile c " +
    //             "JOIN c.user u " + 
    //             "LEFT JOIN c.location l " +
    //             "WHERE " +
    //             "(:keyword IS NULL OR " + 
    //             "       c.fullName LIKE CONCAT('%', :keyword, '%') " + 
    //             "       OR u.email LIKE CONCAT('%', :keyword, '%') " +
    //             "       OR c.headline LIKE CONCAT('%', :keyword, '%') " + 
    //             ") " + // keyword
    //             "AND (:location IS NULL OR l.provinceCity LIKE CONCAT('%', :location, '%')) " + // location
    //             // " AND (:category IS NULL)" + // category, tạm thời bỏ qua sau này sẽ thêm lại
    //             "AND (:#{#experienceLevel} IS NULL OR c.experienceLevel = :#{#experienceLevel}) " + // experienceLevel
    //             "AND ((:#{#educationLevels == null || #educationLevels.isEmpty()}) = true OR c.educationLevel IN :#{#educationLevels}) " + // educationLevels
    //             "AND (:#{#gender} IS NULL OR c.gender = :#{#gender}) " + // gender
    //             "AND (c.isPublic = true)" // public
    // )
    // Page<CandidatePageResponse> searchCandidates(
    //     @Param("keyword") String keyword,
    //     @Param("location") String location,
    //     @Param("category") String category,
    //     @Param("experienceLevel") ExperienceLevel experienceLevel,
    //     @Param("educationLevels") List<EducationLevel> educationLevels,
    //     @Param("gender") Gender gender,
    //     @Param("companyId") Long companyId,
    //     Pageable pageable
    // );

    @Query("SELECT c.fullName FROM CandidateProfile c WHERE c.user = :user")
    String findFullNameByUser(User user);

    @Query("SELECT c.avatarUrl FROM CandidateProfile c WHERE c.user = :user")
    String findAvatarByUser(User user);
}
