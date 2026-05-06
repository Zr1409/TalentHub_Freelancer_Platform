package com.backend.dto.response.account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.backend.enums.StatusAccount;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AdminAccountDTOResponse {
    private Long id;
    private String email;
    private String role;
    private StatusAccount status;
    private Boolean mfaEnabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
