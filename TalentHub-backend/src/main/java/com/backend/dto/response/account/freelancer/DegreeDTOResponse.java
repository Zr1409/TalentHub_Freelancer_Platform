package com.backend.dto.response.account.freelancer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DegreeDTOResponse {
    private Long id;
    private String degreeTitle;
    private Long quantityEducation;
}
