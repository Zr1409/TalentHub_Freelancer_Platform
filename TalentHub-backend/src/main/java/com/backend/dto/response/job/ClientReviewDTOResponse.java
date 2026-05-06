package com.backend.dto.response.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ClientReviewDTOResponse {
    private Long id;
    private Float rating;
    private String note;
    private String reviewerName;

    private String projectTitle;
    private LocalDateTime projectStartDate;
    private Long projectDuration;
    private BigDecimal projectBudget;

    private String freelancerAvatar;
}