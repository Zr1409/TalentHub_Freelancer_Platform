package com.backend.dto.request.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MfaLoginVerificationRequest {
    private String email;
    private String tempToken;
    private String mfaCode;
    private Double lat;
    private Double lng;
}