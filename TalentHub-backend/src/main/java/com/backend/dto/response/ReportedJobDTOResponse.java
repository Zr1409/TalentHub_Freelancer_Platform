package com.backend.dto.response;

import lombok.Getter;
import lombok.Setter;
import com.backend.dto.response.job.JobDTOResponse;
import com.backend.enums.ReportedJobStatus;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReportedJobDTOResponse {
    private Long id;
    private String jobTitle;
    private String reasonFreelancer;
    private String reasonAdmin;
    private String description;
    private ReportedJobStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long freelancerId;
    private Long jobId;
    private String fullName;
    private JobDTOResponse job;
    private String image;
}
