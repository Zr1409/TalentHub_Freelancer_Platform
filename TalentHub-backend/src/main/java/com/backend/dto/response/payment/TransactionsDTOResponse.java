package com.backend.dto.response.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.backend.enums.ActivityType;
import com.backend.enums.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class TransactionsDTOResponse {
    private Long id;

    private BigDecimal money;

    private ActivityType activity;

    private LocalDateTime createdAt;

    private String description;

    private TransactionStatus status;

}
