package com.ptit.thesis.smartrecruit.scheduler;

import java.time.LocalDate;
import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ptit.thesis.smartrecruit.entity.Job;
import com.ptit.thesis.smartrecruit.enums.JobStatus;
import com.ptit.thesis.smartrecruit.listener.JobExpiredEvent;
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
    ApplicationEventPublisher applicationEventPublisher;

    @Scheduled(cron = "0 30 2 * * *")
    @Transactional
    public void updateJobStatus() {
        log.info("Update job status");
        
        List<Job> jobs = jobRepository.findByStatusAndExpirationDateBefore(JobStatus.ACTIVE, LocalDate.now());

        if (jobs.isEmpty()) {
            log.info("No job to update status");
            return;
        }

        jobs.forEach(job -> {
            job.setStatus(JobStatus.EXPIRED);
            applicationEventPublisher.publishEvent(new JobExpiredEvent(job));
        });

        jobRepository.saveAll(jobs);
        log.info("Update job status successfully");
    }
}
