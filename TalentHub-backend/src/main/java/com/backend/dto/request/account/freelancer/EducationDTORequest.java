package com.backend.dto.request.account.freelancer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EducationDTORequest {

    private Date startDate;

    private Date endDate;

    private String description;

    private Long schoolId;
    private Long degreeId;
    private Long majorId;
    private Long freelancerId;
}
