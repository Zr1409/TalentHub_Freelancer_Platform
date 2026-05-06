package com.backend.dto.request.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerJobDTORequest {
    private long jobId;
    private long freelancerId;
    private long cvId;
}
