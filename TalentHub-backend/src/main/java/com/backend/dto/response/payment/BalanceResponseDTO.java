package com.backend.dto.response.payment;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class BalanceResponseDTO {
    private BigDecimal balance;
    private BigDecimal latestDeposit;
    private LocalDateTime latestDepositDate;
    private BigDecimal todaySpending;
    private LocalDateTime latestSpendingDate;
    private LocalDateTime oldestTransactionDate;

}
