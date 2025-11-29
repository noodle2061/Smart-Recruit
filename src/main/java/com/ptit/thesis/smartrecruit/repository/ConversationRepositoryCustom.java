package com.ptit.thesis.smartrecruit.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import com.ptit.thesis.smartrecruit.dto.response.ConversationResponse;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Company;

public interface ConversationRepositoryCustom {
    Slice<ConversationResponse> findCandidateConversations(CandidateProfile candidate, Pageable pageable, Boolean isRead);

    Slice<ConversationResponse> findCompanyConversations(Company company, Pageable pageable, Boolean isRead);
}
