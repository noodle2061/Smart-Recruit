package com.ptit.thesis.smartrecruit.service;

import com.ptit.thesis.smartrecruit.entity.User;

public interface SavedCandidateService {
    void saveCandidate(Long candidateId, User user);
    void unsaveCandidate(Long candidateId, User user);
}
