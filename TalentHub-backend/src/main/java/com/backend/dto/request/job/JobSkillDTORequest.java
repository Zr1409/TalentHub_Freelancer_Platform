package com.backend.dto.request.job;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JobSkillDTORequest {
    private Long jobId;
    private Long skillId;
}
