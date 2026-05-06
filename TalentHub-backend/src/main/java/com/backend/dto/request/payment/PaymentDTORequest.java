package com.backend.dto.request.payment;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTORequest {
    @NotNull(message = "Default status is required")
    private Boolean isDefault;

    @Positive(message = "Bank account ID must be a positive number")
    private Long bankAccountId;

    @Positive(message = "E-Wallet account ID must be a positive number")
    private Long eWalletAccountId;

    @NotNull(message = "Account ID is required")
    @Positive(message = "Account ID must be a positive number")
    private Long accountId;
}
