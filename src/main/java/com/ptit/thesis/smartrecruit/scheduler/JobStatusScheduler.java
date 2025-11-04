package com.ptit.thesis.smartrecruit.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.ptit.thesis.smartrecruit.entity.Job;
import com.ptit.thesis.smartrecruit.enums.JobStatus;
import com.ptit.thesis.smartrecruit.repository.JobRepository;

import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Slf4j
public class JobStatusScheduler {
    
    JobRepository jobRepository;

    @Scheduled(cron = "0 50 4 * * ?")
    @Transactional
    public void updateJobStatus() {
        log.info("Update job status");
        
        List<Job> jobs = jobRepository.findByStatusAndExpirationDateBefore(JobStatus.ACTIVE, LocalDate.now());

        if (jobs.isEmpty()) {
            log.info("No job to update status");
            return;
        }

        jobs.forEach(job -> job.setStatus(JobStatus.EXPIRED));

        jobRepository.saveAll(jobs);
        log.info("Update job status successfully");
    }
}
