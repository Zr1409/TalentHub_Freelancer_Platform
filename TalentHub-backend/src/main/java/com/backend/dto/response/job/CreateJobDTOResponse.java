package com.backend.dto.response.job;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import com.backend.enums.StatusJob;
import com.backend.enums.TypePayment;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Builder
public class CreateJobDTOResponse {
    private Long jobId;
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
    private Date createdAt;
    private String createdTimeFormatted;
}
