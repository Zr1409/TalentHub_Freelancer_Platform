package com.backend.dto.response.cv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CVWithJobsDTO {
    private Long id;
    private String title;
    private String url;
    private Boolean status;
    private List<CVJobDTO> jobs;
}

