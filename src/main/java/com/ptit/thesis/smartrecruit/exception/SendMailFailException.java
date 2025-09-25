package com.ptit.thesis.smartrecruit.exception;

public class SendMailFailException extends RuntimeException {
    public SendMailFailException(String message) {
        super(message);
    }
}
