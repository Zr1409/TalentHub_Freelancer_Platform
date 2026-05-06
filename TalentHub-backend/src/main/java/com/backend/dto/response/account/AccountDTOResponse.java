package com.backend.dto.response.account;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.backend.dto.UserDTO;
import com.backend.enums.RoleUser;
import com.backend.enums.StatusAccount;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class AccountDTOResponse {
    private Long id;
    private String email;
    private RoleUser role;
    private StatusAccount status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDTO user;
//    private List<PaymentDTOResponse> payments;
}
