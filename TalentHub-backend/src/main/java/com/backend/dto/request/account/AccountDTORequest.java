package com.backend.dto.request.account;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import com.backend.enums.RoleUser;
import com.backend.enums.StatusAccount;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountDTORequest {


    @NotBlank(message = "Email is required")
    @NotNull(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    @NotNull(message = "Role cannot be null")
    private RoleUser role;

    //    @NotNull(message = "Status is required")
    private StatusAccount status;

    //    @NotBlank(message = "First name is required")
    private String firstName;

    //    @NotBlank(message = "Last name is required")
    private String lastName;

    //    @NotBlank(message = "Phone number is required")
    private String phoneNumber;

    private String province;
    private String ward;

    private String title;

    private String introduction;
    @NotNull(message = "lat is required")
    private double lat = 0;
    @NotNull(message = "lng is required")
    private double lng = 0;
}
