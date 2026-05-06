package com.backend.dto.response.account.freelancer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
@AllArgsConstructor
public class SchoolDTOResponse {

    private Long id;
    private String schoolName;
    private Long quantityEducation;
}