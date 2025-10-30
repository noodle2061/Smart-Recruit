package com.ptit.thesis.smartrecruit.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ptit.thesis.smartrecruit.entity.CandidateCompany;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.FollowType;
import com.ptit.thesis.smartrecruit.exception.ConflictException;
import com.ptit.thesis.smartrecruit.repository.CandidateCompanyRepository;
import com.ptit.thesis.smartrecruit.repository.CandidateProfileRepository;
import com.ptit.thesis.smartrecruit.repository.CompanyRepository;
import com.ptit.thesis.smartrecruit.service.CandidateCompanyFollowService;

import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CandidateCompanyFollowServiceImpl implements CandidateCompanyFollowService {

    CandidateCompanyRepository candidateCompanyRepository;
    CompanyRepository companyRepository;
    CandidateProfileRepository candidateProfileRepository;

    @Override
    public void companySaveCandidate(Long candidateId, User user) {
        Company company = companyRepository.findByUser(user)
                        .orElseThrow(() -> new EntityNotFoundException("Company not found for user: " + user.getId()));
        addFollow(company.getId(), candidateId, FollowType.COMPANY_FOLLOW_CANDIDATE);
    }

    @Override
    public void companyUnsaveCandidate(Long candidateId, User user) {
        Company company = companyRepository.findByUser(user)
                        .orElseThrow(() -> new EntityNotFoundException("Company not found for user: " + user.getId()));
        removeFollow(company.getId(), candidateId, FollowType.COMPANY_FOLLOW_CANDIDATE);
    }

    @Override
    public void candidateFollowCompany(Long companyId, User user) {
        CandidateProfile candidateProfile = candidateProfileRepository.findByUser(user)
                        .orElseThrow(() -> new EntityNotFoundException("Candidate profile not found for user: " + user.getId()));
        addFollow(companyId, candidateProfile.getId(), FollowType.CANDIDATE_FOLLOW_COMPANY);
    }

    @Override
    public void candidateUnfollowCompany(Long companyId, User user) {
        CandidateProfile candidateProfile = candidateProfileRepository.findByUser(user)
                        .orElseThrow(() -> new EntityNotFoundException("Candidate profile not found for user: " + user.getId()));
        removeFollow(companyId, candidateProfile.getId(), FollowType.CANDIDATE_FOLLOW_COMPANY);
    }

    @Override
    public void addFollow(Long companyId, Long candidateId, FollowType type) {
        Optional<CandidateCompany> candidateCompany = candidateCompanyRepository.findByIdAndType(companyId, candidateId, type);
        if (candidateCompany.isPresent()) {
            throw new ConflictException((type == FollowType.COMPANY_FOLLOW_CANDIDATE) ? "Candidate already saved" : "Company already saved");
        } else {
            CandidateCompany newCandidateCompany = new CandidateCompany();
            newCandidateCompany.setCandidate(candidateProfileRepository.findById(candidateId)
                    .orElseThrow(() -> new EntityNotFoundException("Candidate profile not found for id: " + candidateId)));
            newCandidateCompany.setCompany(companyRepository.findById(companyId)
                    .orElseThrow(() -> new EntityNotFoundException("Company not found for id: " + companyId)));
            newCandidateCompany.setType(type);
            candidateCompanyRepository.save(newCandidateCompany);
        }
        
    }

    @Override
    public void removeFollow(Long companyId, Long candidateId, FollowType type) {
        Optional<CandidateCompany> candidateCompany = candidateCompanyRepository.findByIdAndType(companyId, candidateId, type);
        if (candidateCompany.isPresent()) {
            candidateCompanyRepository.delete(candidateCompany.get());
        } else {
            throw new EntityNotFoundException((type == FollowType.COMPANY_FOLLOW_CANDIDATE) ? "Company have not saved candidate yet" : "Candidate have not saved company yet");
        }
    }
    
}
