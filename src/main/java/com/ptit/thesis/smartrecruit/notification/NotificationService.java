package com.ptit.thesis.smartrecruit.notification;

public interface NotificationService {
    public void sendVerificationMessage(String to, String userName, String verificationUrl);
}
