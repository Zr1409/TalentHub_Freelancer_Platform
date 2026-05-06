package com.backend.exception;

import jakarta.servlet.http.HttpServletResponse;


public class AuthenticationException extends RuntimeException {

    private static final long serialVersionUID = 2954751231802280170L;

    private final int statusCode;

    public AuthenticationException(String message) {
        super(message);
        this.statusCode = HttpServletResponse.SC_EXPECTATION_FAILED;
    }

    public AuthenticationException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
