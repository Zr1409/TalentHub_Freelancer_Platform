package com.backend.dto.request.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.enums.StatusJob;
import com.backend.enums.TypePayment;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class JobDetailDTORequest {
    private String title;
    private String description;
    private String scope;
    private BigDecimal hourWork;
    private Long duration;
    private Boolean jobOpportunity;
    private BigDecimal fromPrice;
    private BigDecimal toPrice;
    private String typePrice;
    private TypePayment typePayment;
    private StatusJob statusJob;
    private Long clientId;
    private Long categoryId;
    private List<Long> skillId;
}
