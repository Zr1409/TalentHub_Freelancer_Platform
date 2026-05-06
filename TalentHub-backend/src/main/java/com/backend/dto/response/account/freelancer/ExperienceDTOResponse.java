package com.backend.dto.response.account.freelancer;

import lombok.*;
import com.backend.entity.child.account.freelancer.Freelancer;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExperienceDTOResponse {
    private Long id;
    private String companyName;
    private String position;
    private LocalDate startDate;
    private LocalDate endDate;
    private String description;
    private String status;
    private Freelancer freelancer;
}
