package com.backend.exception;

import jakarta.servlet.http.HttpServletResponse;

public class UnauthorizedException extends RuntimeException {

    private static final long serialVersionUID = 2954751231802280170L;

    private final int statusCode;

    public UnauthorizedException(String message) {
        super(message);
        this.statusCode = HttpServletResponse.SC_UNAUTHORIZED;
    }

    public UnauthorizedException(String message, int statusCode) {
        super(message);
        this.statusCode = statusCode;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
