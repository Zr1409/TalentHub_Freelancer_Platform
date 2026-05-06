package com.backend.dto.response.account.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.backend.dto.response.job.ClientReviewDTOResponse;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDetailDTOResponse {
    private Long clientId;
    private Double fromPrice;
    private Double toPrice;
    private Integer jobsCount;

    private Long userId;
    private String firstName;
    private String lastName;
    private String province;
    private String ward;
    private String title;
    private String introduction;
    private String image;

    private String email;

    private Float averageRating;

    private List<CompanyDTOResponse> companies;

    private List<ClientReviewDTOResponse> reviews;
}