package com.backend.dto.response.account.freelancer;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdateHourlyRateDTOResponse {
    private Long freelancerId;
    private BigDecimal hourlyRate;
}
