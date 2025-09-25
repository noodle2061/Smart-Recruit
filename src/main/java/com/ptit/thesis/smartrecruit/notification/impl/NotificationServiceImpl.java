package com.ptit.thesis.smartrecruit.notification.impl;

import org.springframework.stereotype.Service;

import com.ptit.thesis.smartrecruit.notification.MailService;
import com.ptit.thesis.smartrecruit.notification.NotificationService;
import com.ptit.thesis.smartrecruit.utils.ResourceReader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    
    private final MailService mailService;

    @Override
    public void sendVerificationMessage(String to, String userName, String verificationUrl) {
        String subject = "SmartRecruit - Xác thực tài khoản của bạn";
        String content = ResourceReader.readEmailTemplate("verify-registration-email.html");
        content = content.replace("{{userName}}", userName);
        content = content.replace("{{verificationUrl}}", verificationUrl);
        mailService.sendHtmlMessage(to, subject, content);
    }
}
