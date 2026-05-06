package com.backend.dto.response.account.freelancer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.enums.StatusAccount;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerInfoDTOResponse {
    private Long id;
    private String name;
    private String title;
    private String avatar;
    private Float rating;
    private String province;
    private String ward;
    private List<String> skills;
    private BigDecimal hourlyRate;
    private StatusAccount status;
}
