package com.backend.dto.response.account.freelancer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectDTOResponse {
    private Long id;
    private String title;
    private String tech;
    private String description;
    private String link;
    private String image;
    private Long freelancerId;
    private String freelancerName;


}