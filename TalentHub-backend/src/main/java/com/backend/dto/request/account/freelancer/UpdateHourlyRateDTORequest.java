package com.backend.dto.request.account.freelancer;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

@Getter
@Setter
public class UpdateHourlyRateDTORequest {
    @NotNull(message = "Freelancer ID cannot be null")
    private Long freelancerId;

    @NotNull(message = "Hourly rate cannot be null")
    @Positive(message = "Hourly rate must be positive")
    private BigDecimal hourlyRate;
}
