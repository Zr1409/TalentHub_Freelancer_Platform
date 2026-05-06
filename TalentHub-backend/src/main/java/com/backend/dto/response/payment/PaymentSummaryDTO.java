package com.backend.dto.response.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.enums.ActivityType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentSummaryDTO {
    private ActivityType activity;
    private BigDecimal totalAmount;
    private LocalDateTime latestTransactionDate;
//    private LocalDateTime oldestTransactionDate;

}
