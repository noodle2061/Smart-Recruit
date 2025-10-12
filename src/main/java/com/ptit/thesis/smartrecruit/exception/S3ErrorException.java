package com.ptit.thesis.smartrecruit.exception;

public class S3ErrorException extends RuntimeException {
    public S3ErrorException(String message) {
        super(message);
    }
}
