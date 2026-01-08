package com.ptit.thesis.smartrecruit.listener;

import java.util.List;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.ptit.thesis.smartrecruit.entity.Application;
import com.ptit.thesis.smartrecruit.entity.CandidateProfile;
import com.ptit.thesis.smartrecruit.entity.Job;
import com.ptit.thesis.smartrecruit.entity.User;
import com.ptit.thesis.smartrecruit.enums.JobApplicationStatus;
import com.ptit.thesis.smartrecruit.enums.NotificationType;
import com.ptit.thesis.smartrecruit.repository.ApplicationRepository;
import com.ptit.thesis.smartrecruit.service.NotificationService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JobExpiredListener {
    
    ApplicationRepository applicationRepository;
    NotificationService notificationService;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleJobExpiredEvent(JobExpiredEvent event) {
        Job job = event.job();
        
        List<Application> applications = applicationRepository.findByJobAndStatus(job, JobApplicationStatus.PROCESSING);

        if (applications.isEmpty()) {
            return;
        }

        applications.forEach(application -> {
            application.setStatus(JobApplicationStatus.REJECTED);
            CandidateProfile candidate = application.getCandidate();
            User candidateUser = candidate.getUser();
            notificationService.pushNotification(null, candidateUser, String.format("Công việc %s tại %s đã đóng. Đơn ứng tuyển của bạn đã cập nhật trạng thái", job.getTitle(), job.getCompany().getName()), NotificationType.JOB_APPLICATION, job.getId());
        });

        applicationRepository.saveAll(applications);
    }
}
