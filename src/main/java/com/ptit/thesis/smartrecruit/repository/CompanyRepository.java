package com.ptit.thesis.smartrecruit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.dto.common.CompanyBasicInfoDTO;
import com.ptit.thesis.smartrecruit.dto.response.CompanyStatResponse;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.entity.User;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long>, JpaSpecificationExecutor<Company>, CompanyRepositoryCustom {
    Optional<Company> findByUser(User user);
    boolean existsByName(String name);

    @Query("SELECT new com.ptit.thesis.smartrecruit.dto.common.CompanyBasicInfoDTO(" +
    "c.name, c.logoUrl, c.foundedIn, c.website, c.email, c.phone, c.teamSize)" +
    "FROM Company c WHERE c.user = :user")
    CompanyBasicInfoDTO findBasicInfoByUser(User user);

    @Query("SELECT new com.ptit.thesis.smartrecruit.dto.common.CompanyBasicInfoDTO(" +
    "c.name, c.logoUrl, c.foundedIn, c.website, c.email, c.phone, c.teamSize)" +
    "FROM Company c WHERE c.id = :id")
    CompanyBasicInfoDTO findBasicInfoById(Long id);

    @Query("SELECT c.logoUrl FROM Company c WHERE c.user = :user")
    String findAvatarByUser(User user);

    boolean existsByUser(User user);

    @Query("""
            SELECT new com.ptit.thesis.smartrecruit.dto.response.CompanyStatResponse( 
            (SELECT COUNT(j) FROM Job j WHERE j.status = com.ptit.thesis.smartrecruit.enums.JobStatus.ACTIVE AND j.company.id = c.id), 
            (SELECT COUNT(cc) FROM CandidateCompany cc WHERE cc.company.id = :id AND cc.type = com.ptit.thesis.smartrecruit.enums.FollowType.COMPANY_FOLLOW_CANDIDATE))
            FROM Company c 
            WHERE c.id = :id
    """)
    CompanyStatResponse getCompanyStat(Long id);
}
