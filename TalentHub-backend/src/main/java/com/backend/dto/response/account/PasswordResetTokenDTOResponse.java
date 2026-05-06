package com.backend.dto.response.account;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetTokenDTOResponse {
    private String message;
    private boolean success;
}
