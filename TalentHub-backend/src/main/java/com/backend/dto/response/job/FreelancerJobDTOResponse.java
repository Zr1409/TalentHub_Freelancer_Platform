package com.backend.dto.response.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.enums.StatusFreelancerJob;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerJobDTOResponse {
    private long id;
    private boolean isSaved;
    private StatusFreelancerJob status;
    private long jobId;
    private long freelancerId;
    private String urlCV;
}
