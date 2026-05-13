
package com.backend.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    INVALID_TOKEN(1001, "Invalid Token")
    ;

    private final Integer status;
    private final String message;

    // Constructor yêu cầu tham số
    ErrorCode(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
