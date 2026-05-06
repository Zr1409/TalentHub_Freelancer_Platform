package com.backend.dto.response.account.freelancer;

import lombok.Data;

@Data
public class FreelancerSkillDTOResponse {
    private Long id;
    private Long freelancerId;
    private Long skillId;
    private String skillName;
}