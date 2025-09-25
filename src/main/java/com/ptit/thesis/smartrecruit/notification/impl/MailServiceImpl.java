package com.ptit.thesis.smartrecruit.notification.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.ptit.thesis.smartrecruit.exception.SendMailFailException;
import com.ptit.thesis.smartrecruit.notification.MailService;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailServiceImpl implements MailService {
    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendSimpleMessage(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
        } catch (Exception e) {
            throw new SendMailFailException("Error sending email: " + e.getMessage());
        }
    }

    @Override
    @Async
    public void sendHtmlMessage(String to, String subject, String htmlBody) {
        log.info("Sending html email to {}", to);
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setFrom(fromEmail);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlBody, true);
            mailSender.send(message);
            log.info("Html email sent successfully to {}", to);
        } catch (Exception e) {
            log.error("Error sending html email to {}: {}", to, e.getMessage());
            throw new SendMailFailException("Error sending email: " + e.getMessage());
        }
    }
}
