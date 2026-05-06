package com.backend.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.backend.enums.ActivityType;
import com.backend.enums.TransactionStatus;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class ResultPaymentResponseDTO {
    private String codeVnp;
    private String fristName;
    private String lastName;
    private BigDecimal amount;
    private ActivityType activity;
    private String description;
    private TransactionStatus transactionStatus;
}
