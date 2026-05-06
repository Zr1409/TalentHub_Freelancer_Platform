package com.backend.dto.request.account.freelancer;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class FreelancerDTORequest {

    private String image;
    private BigDecimal hourlyRate;
    private String description;
    private Long categoryId;
    private Long userId;
}
