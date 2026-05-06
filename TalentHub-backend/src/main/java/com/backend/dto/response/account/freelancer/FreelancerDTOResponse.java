package com.backend.dto.response.account.freelancer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class FreelancerDTOResponse {

    private Long id;
    private String name;
//    private String image;
    private BigDecimal hourlyRate;
    private String description;
    private String categoryName;
    private Long userId;
    private String avatar;
    private Float rating;
    private List<String> skills;

}

