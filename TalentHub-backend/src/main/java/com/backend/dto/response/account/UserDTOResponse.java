package com.backend.dto.response.account;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.backend.enums.RoleUser;
import com.backend.enums.StatusAccount;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTOResponse {
    private Long id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String province;
    private String ward;
    private String title;
    private String introduction;
    private String image;
    private RoleUser role;
    private StatusAccount status;
    private Boolean mfaEnabled;
}