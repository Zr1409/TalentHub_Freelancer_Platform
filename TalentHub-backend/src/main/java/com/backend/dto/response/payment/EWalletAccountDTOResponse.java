package com.backend.dto.response.payment;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EWalletAccountDTOResponse {

    private Long id;
    private String eWalletName;
    private String phoneNumber;
    private String email;
    private LocalDateTime startDate;
}
