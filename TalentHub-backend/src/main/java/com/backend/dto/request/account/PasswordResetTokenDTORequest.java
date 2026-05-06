package com.backend.dto.request.account;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetTokenDTORequest {
    private String code;
    private String email;
    private String newPassword;

}
