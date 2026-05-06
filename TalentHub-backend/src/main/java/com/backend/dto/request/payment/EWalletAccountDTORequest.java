package com.backend.dto.request.payment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class EWalletAccountDTORequest {
    @NotBlank(message = "E-Wallet name is required")
    private String eWalletName;

    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotNull(message = "Start date is required")
    private String startDate;

    @NotBlank(message = "Payment id is required")
    private Long paymentId;

}

