package com.backend.dto.response.job;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class PostJobsDTOResponse {
    private Long id;
    private String title;
    private String type;
    private Long applicants;
    private LocalDateTime postedDate;
    private String status;
    private Date endDate;
    private long remainingTimeInHours;
    private String remainingTimeFormatted;
    private Date createdAt;
    private String createdTimeFormatted;
}
