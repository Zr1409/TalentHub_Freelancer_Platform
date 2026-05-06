package com.backend.dto.response.account.freelancer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.entity.child.account.freelancer.Education;
import com.backend.entity.child.account.freelancer.Experience;
import com.backend.entity.child.account.freelancer.Project;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class FreelancerDetailDTOResponse {
    private long id;
    private String name;
    private String title;
    private String avatar;
    private String province;
    private String ward;
    private float rating;
    private long completeProject;
    private BigDecimal hourlyRate;
    private String overview;
    private long userId;
    private String categoryTitle;
    private List<String> skills;
    private List<Education> educations;
    private List<Experience> experiences;
    private List<Project> projects;

    private double lat;
    private double lng;
}
