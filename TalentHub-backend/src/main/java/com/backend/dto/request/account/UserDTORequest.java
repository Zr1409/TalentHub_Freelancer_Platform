package com.backend.dto.request.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTORequest {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String province;
    private String ward;
    private String title;
    private String introduction;
    private String image;
}