package com.backend.dto.request.job;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class JobDTORequest {
    private String title;
    private String description;
    private String scope;
    private BigDecimal hourWork;
    private Long duration;
    private Boolean jobOpportunity;
    private BigDecimal fromPrice;
    private BigDecimal toPrice;
    private String typePrice;
    private String typePayment;
    private Long clientId;
    private Long categoryId;
}
