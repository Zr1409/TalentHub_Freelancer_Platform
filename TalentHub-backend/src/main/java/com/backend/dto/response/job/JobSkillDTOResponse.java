package com.backend.dto.response.job;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class JobSkillDTOResponse {

    private Long id;
    private String jobName;
    private String skillName;

}
