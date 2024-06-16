package com.handelsbank.ecommerceApi.exceptions;

import org.springframework.http.HttpStatus;

public class ApiError {
    private String message;
    private String error;
    private HttpStatus status;

    public ApiError(String message, String error, HttpStatus status) {
        this.message = message;
        this.error = error;
        this.status = status;
    }

    // Getters and setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }
}
