package com.backend.dto.response.account;

import lombok.*;
import com.backend.enums.RoleUser;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationDtoResponse {
    private Long userId;
    private RoleUser role;
    private Long freelancerId;
    private Long clientId;
    private String accessToken;
    private double lat;
    private double lng;
    private String email;
    private boolean mfaEnabled;
    private Long expiresIn;
//    private String refreshToken;
}
