package com.backend.dto.response.account.freelancer;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class FreelancerAdminDTOResponse {
    private Long id;
    private String name;
    private String email;
    private BigDecimal hourlyRate;
    private String description;
    private String categoryName;
    private Long userId;
    private String avatar;
    private Float rating;
    private List<String> skills;
}
