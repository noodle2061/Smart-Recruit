package com.ptit.thesis.smartrecruit.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.CandidateCompany;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.FollowType;
import com.ptit.thesis.smartrecruit.repository.CandidateCompanyRepository;
import com.ptit.thesis.smartrecruit.repository.CandidateProfileRepository;
import com.ptit.thesis.smartrecruit.repository.CompanyRepository;
import com.ptit.thesis.smartrecruit.service.SavedCandidateService;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Repository
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SavedCandidateServiceImpl implements SavedCandidateService {

    CandidateCompanyRepository candidateCompanyRepository;
    CandidateProfileRepository candidateProfileRepository;
    CompanyRepository companyRepository;

    @Override
    @Transactional
    public void saveCandidate(Long candidateId, User user) {
        CandidateProfile candidateProfile = candidateProfileRepository.findById(candidateId)
                                                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with ID: " + candidateId));
        
        Company company = companyRepository.findByUser(user)
                                            .orElseThrow(() -> new EntityNotFoundException("Company not found for user with ID: " + user.getId()));

        Optional<CandidateCompany> candidateCompanyOpt = candidateCompanyRepository.findByCandidateAndCompany(candidateProfile, company);

        if (candidateCompanyOpt.isEmpty()) {
            CandidateCompany candidateCompany = new CandidateCompany();
            candidateCompany.setCandidate(candidateProfile);
            candidateCompany.setCompany(company);
            candidateCompany.setType(FollowType.COMPANY_FOLLOW_CANDIDATE);
            candidateCompanyRepository.save(candidateCompany);
        }
    }

    @Override
    public void unsaveCandidate(Long candidateId, User user) {
        CandidateProfile candidateProfile = candidateProfileRepository.findById(candidateId)
                                                .orElseThrow(() -> new EntityNotFoundException("Candidate not found with ID: " + candidateId));
        
        Company company = companyRepository.findByUser(user)
                                            .orElseThrow(() -> new EntityNotFoundException("Company not found for user with ID: " + user.getId()));

        candidateCompanyRepository.findByCandidateAndCompany(candidateProfile, company).ifPresent(candidateCompany -> candidateCompanyRepository.delete(candidateCompany));
        
    }
    
}
