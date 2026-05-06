package com.backend.dto.response.account.freelancer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FreelancerWithJobsDTOResponse {
    private Long userId;
    private String fullName;
    private String avatar;
    private Float rating;
    private List<JobInfo> jobs;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class JobInfo {
        private Long id;
        private String title;
        private String status;
    }
}