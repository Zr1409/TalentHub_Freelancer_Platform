package com.backend.dto.response.account.freelancer;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateFreelancerDTOResponse {
    private BigDecimal hourlyRate;
    private String description;
    private Long categoryId;
    private Long userId;
}
