package com.backend.dto.response.account.client;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTOResponse {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private Boolean mfaEnabled;
    private String province;
    private String ward;
    private String image;
    private Double fromPrice;
    private Double toPrice;
    private String typePrice;
    private Integer jobsCount;
    private Integer appointmentsCount;
    // Other existing fields
}