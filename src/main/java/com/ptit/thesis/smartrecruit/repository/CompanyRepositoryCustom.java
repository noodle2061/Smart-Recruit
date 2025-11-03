package com.ptit.thesis.smartrecruit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.ptit.thesis.smartrecruit.dto.response.CompanyPageResponse;
import com.ptit.thesis.smartrecruit.enums.CompanyTeamSize;
import com.ptit.thesis.smartrecruit.enums.IndustryType;
import com.ptit.thesis.smartrecruit.enums.OrganizationType;

public interface CompanyRepositoryCustom {
    Page<CompanyPageResponse> searchCompany(
                        String keyword, 
                        String provinceCity, 
                        OrganizationType organizationType, 
                        IndustryType industryType,
                        CompanyTeamSize teamSize,
                        Integer foundedIn,
                        Pageable pageable);
}
