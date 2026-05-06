package com.backend.dto.response.account.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CompanyDTOResponse {

    private Long id;

    private String companyName;

    private String companyImage;

    private String address;

    private String phoneContact;

    private String industry;

    private Long clientId;
}
