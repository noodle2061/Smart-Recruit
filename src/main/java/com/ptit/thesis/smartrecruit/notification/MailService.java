package com.ptit.thesis.smartrecruit.notification;

public interface MailService {
    public void sendSimpleMessage(String to, String subject, String body);
    public void sendHtmlMessage(String to, String subject, String htmlBody);
}
