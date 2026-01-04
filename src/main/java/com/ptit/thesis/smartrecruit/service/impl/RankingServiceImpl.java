package com.ptit.thesis.smartrecruit.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ptit.thesis.smartrecruit.dto.response.CompanyPageResponse;
import com.ptit.thesis.smartrecruit.dto.response.JobPageResponse;
import com.ptit.thesis.smartrecruit.service.RankingService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class RankingServiceImpl implements RankingService {

    @Override
    public List<JobPageResponse> getHotJobs(int limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHotJobs'");
    }

    @Override
    public List<CompanyPageResponse> getHotCompanies(int limit) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getHotCompanies'");
    }
    
    
    private void updateHotJobs() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateHotJobs'");
    }

    private void updateHotCompanies() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateHotCompanies'");
    }
}
