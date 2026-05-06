package com.backend.dto.response.job;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SkillDTOResponse {
    private Long id;
    private String skillName;
    private Long quantityFreelancerSkill;
    private Long quantityJobSkill;
}
