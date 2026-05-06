package com.backend.dto.response.payment;

import com.google.type.DateTime;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BankAccountDTOResponse {
    private Long id;
    private String bankName;
    private Long bankAccountNumber;
    private String branch;
    private LocalDateTime startDate;
    private Boolean status;
}
