package com.backend.dto.response.job;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class ApplyJobsDTOResponse {
    private Long id;
    private String jobTitle;
    private String companyName;
    private Long clientId;
    private String firstName;
    private String lastName;
    private String jobType;
    private BigDecimal hourWork;
    private BigDecimal fromPrice;
    private BigDecimal toPrice;
    private String status;
    private Date applyDate;
    private String description;
    private List<String> SkillNames;
    private Long freelancerJobId;
    private Date endDate;
    private long remainingTimeInHours;
    private String remainingTimeFormatted;
    private Date createdAt;
    private String createdTimeFormatted;
}
