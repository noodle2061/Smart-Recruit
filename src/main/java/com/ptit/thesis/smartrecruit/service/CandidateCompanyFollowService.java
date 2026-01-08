package com.ptit.thesis.smartrecruit.service;

import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.FollowType;

public interface CandidateCompanyFollowService {
    void companySaveCandidate(Long candidateId, User user);
    void companyUnsaveCandidate(Long candidateId, User user);
    void candidateFollowCompany(Long companyId, User user);
    void candidateUnfollowCompany(Long companyId, User user);
    void addFollow(Long companyId, Long candidateId, FollowType type);
    void removeFollow(Long companyId, Long candidateId, FollowType type);
}
