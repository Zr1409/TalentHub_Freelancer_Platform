package com.backend.dto.request;

import lombok.Getter;
import lombok.Setter;
import com.backend.enums.ReportedJobStatus;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
public class ReportedJobDTORequest {
    private String reasonFreelancer;
    private String reasonAdmin;
    private String description;
    private ReportedJobStatus status;
    private Long freelancerId;
    private Long jobId;
    private MultipartFile image;
}
