package com.backend.dto.response.account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MfaChallengeResponse {
    private String email;
    private boolean requiresMfa;
    private String tempToken;
}