package com.tq.hospitalequipmenttracking.exception;

import java.time.LocalDateTime;

public class ErrorResponse {
    private int code;
    private String message;
    private Object errors;
    private LocalDateTime timestamp;

    public ErrorResponse(int code, String message) {
        this.code = code;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public ErrorResponse(int code, String message, Object errors) {
        this.code = code;
        this.message = message;
        this.errors = errors;
        this.timestamp = LocalDateTime.now();
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getErrors() {
        return errors;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
