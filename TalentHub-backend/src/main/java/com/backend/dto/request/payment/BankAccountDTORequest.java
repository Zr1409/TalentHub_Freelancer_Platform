package com.backend.dto.request.payment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.google.type.DateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

@Getter
@Setter
public class BankAccountDTORequest {
    @NotBlank(message = "Bank name cannot be blank")
    private String bankName;

    @NotBlank(message = "Bank account number cannot be null")
    private Long bankAccountNumber;

    @NotBlank(message = "Branch cannot be blank")
    private String branch;

    @NotBlank(message = "Start date is required")
    private String startDate;

    @NotBlank(message = "Status cannot be null")
    private Boolean status;

    @NotNull(message = "Payment id cannot be null")
    private Long paymentId;
}
