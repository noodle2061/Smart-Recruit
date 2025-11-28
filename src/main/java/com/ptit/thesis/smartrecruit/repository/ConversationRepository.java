package com.ptit.thesis.smartrecruit.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Company;
import com.ptit.thesis.smartrecruit.entity.Conversation;

@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long>, ConversationRepositoryCustom {
    Optional<Conversation> findByCandidateAndCompany(CandidateProfile candidate, Company company);
}
