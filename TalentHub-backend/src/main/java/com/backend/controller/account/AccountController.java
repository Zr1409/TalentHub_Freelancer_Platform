package com.backend.controller.account;

import com.backend.dto.request.account.ChangePasswordDTORequest;
import com.backend.dto.request.account.PasswordResetTokenDTORequest;
import com.backend.dto.request.account.VerifyCodeDTORequest;
import com.backend.dto.response.account.*;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.account.AccountDTORequest;
import com.backend.enums.RoleUser;
import com.backend.service.intf.EmailService;
import com.backend.service.intf.account.AccountService;
import com.backend.service.intf.account.PasswordResetTokenService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/account")
public class AccountController {

    private final AccountService accountService;
    private final EmailService emailService;
    private final PasswordResetTokenService passwordResetTokenService;

//    public AccountController(AccountService accountService, EmailService emailService, PasswordResetTokenService passwordResetTokenService) {
//        this.passwordResetTokenService = passwordResetTokenService;
//        this.accountService = accountService;
//        this.emailService = emailService;
//    }

    @PostMapping("/change-password")
    public ResponseObject changePassword(@Valid @RequestBody ChangePasswordDTORequest request) {
        boolean success = accountService.changePassword(request.getEmail(), request.getCurrentPassword(), request.getNewPassword());
        if (success) {
            return ResponseObject.builder()
                    .message("Password changed successfully")
                    .status(HttpStatus.OK.value())
                    .data(true)
                    .build();
        } else {
            return ResponseObject.builder()
                    .message("Current password is incorrect")
                    .status(HttpStatus.BAD_REQUEST.value())
                    .data(false)
                    .build();
        }
    }


    @GetMapping("/admin")
    public ResponseObject<List<AdminAccountDTOResponse>> getAllAccountByAdmin() {
        List<AdminAccountDTOResponse> response = accountService.getAllByAdmin();
        return ResponseObject
                .<List<AdminAccountDTOResponse>>builder()
                .message("Get all accounts successful")
                .status(HttpStatus.OK.value())
                .data(response)
                .build();
    }

    @PostMapping("/admin/ban")
    public ResponseObject<StatusAccountDTOResponse> banAccount(@RequestParam String email) {
        Boolean response = accountService.banAccount(email);
        return ResponseObject
                .<StatusAccountDTOResponse>builder()
                .message("Ban account")
                .status(HttpStatus.OK.value())
                .data(StatusAccountDTOResponse.builder()
                        .status(response ? "Ban account successful" : "Ban account failed")
                        .build())
                .build();
    }

    @PostMapping("/admin/unban")
    public ResponseObject<StatusAccountDTOResponse> unBanAccount(@RequestParam String email) {
        Boolean response = accountService.unBanAccount(email);
        return ResponseObject
                .<StatusAccountDTOResponse>builder()
                .message("UnBan account")
                .status(HttpStatus.OK.value())
                .data(StatusAccountDTOResponse.builder()
                        .status(response ? "Unban account successful" : "Unban account failed")
                        .build())
                .build();
    }

    @PostMapping("/admin/verify")
    public ResponseObject<StatusAccountDTOResponse> activeAccount(@RequestParam String email) {
        Boolean response = accountService.activeAccount(email);
        return ResponseObject
                .<StatusAccountDTOResponse>builder()
                .message("Verify account")
                .status(HttpStatus.OK.value())
                .data(StatusAccountDTOResponse.builder()
                        .status(response ? "Verify account successful" : "Verify account failed")
                        .build())
                .build();
    }

    @GetMapping("/locations")
    public ResponseObject<List<LocationDTOResponse>> getLocations() {
        List<LocationDTOResponse> response = accountService.getLocations();
        return ResponseObject
                .<List<LocationDTOResponse>>builder()
                .message("Get all locations successful")
                .status(HttpStatus.OK.value())
                .data(response)
                .build();
    }


    @GetMapping("/get-nearby")
    public ResponseObject<List<AccountDTOResponse>> getNearbyUsers(
            @RequestParam double lat,
            @RequestParam double lon,
            @RequestParam double distanceInMeters) {
        List<AccountDTOResponse> response = accountService.getNearbyUsers(lat, lon, distanceInMeters);
        return ResponseObject
                .<List<AccountDTOResponse>>builder()
                .message("Get nearby users successful")
                .status(HttpStatus.OK.value())
                .data(response)
                .build();
    }

    @PostMapping("/reset-password")
    public ResponseObject<PasswordResetTokenDTOResponse> resetPassword(@RequestBody PasswordResetTokenDTORequest passwordResetTokenDTORequest) {
        return ResponseObject.<PasswordResetTokenDTOResponse>builder().status(HttpStatus.OK.value())
                .message("Reset password successfully")
                .data(passwordResetTokenService.resetPassword(passwordResetTokenDTORequest)).build()
                ;
    }

    @PostMapping("/verify-code")
    public ResponseObject<PasswordResetTokenDTOResponse> verifyCode(@RequestBody VerifyCodeDTORequest verifyCodeDTORequest) {
        return ResponseObject.<PasswordResetTokenDTOResponse>builder().status(HttpStatus.OK.value())
                .message("Reset password successfully")
                .data(passwordResetTokenService.checkOtp(verifyCodeDTORequest)).build()
                ;
    }

    @PostMapping("/send-otp")
    public ResponseObject<String> sendOtp(@RequestParam String email) {
        if (accountService.checkEmail(email)) {
            boolean emailSent = emailService.sendOtpEmail(email);
            if (emailSent) {
                return ResponseObject.<String>builder()
                        .message("OTP sent successfully")
                        .status(HttpStatus.OK.value())
                        .data("OTP sent successfully to " + email)
                        .build();
            } else {
                return ResponseObject.<String>builder()
                        .message("Failed to send OTP")
                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
                        .build();
            }
        } else {
            return ResponseObject.<String>builder()
                    .message("Email not found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
    }

    @GetMapping("/get-all")
    public ResponseObject<List<AccountDTOResponse>> getAll() {
        List<AccountDTOResponse> response = accountService.getAll();
        return ResponseObject
                .<List<AccountDTOResponse>>builder()
                .message("Get all account successful")
                .status(HttpStatus.OK.value())
                .data(response)
                .build();
    }

    @PostMapping("/register")
    public ResponseObject<AuthenticationDtoResponse> register(@Valid @RequestBody AccountDTORequest request) {
        AuthenticationDtoResponse response = accountService.register(request);
        return ResponseObject.<AuthenticationDtoResponse>builder()
                .message("Register successful")
                .status(200)
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseObject<AccountDTOResponse> get(@PathVariable Long id) {
        return accountService.getById(id)
                .map(response -> ResponseObject.<AccountDTOResponse>builder()
                        .message("Get account successful by id: " + id)
                        .status(HttpStatus.OK.value())
                        .data(response)
                        .build())
                .orElse(ResponseObject.<AccountDTOResponse>builder()
                        .message("Account not found with id: " + id)
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @PostMapping("/choose-role")
    public ResponseEntity<ResponseObject<AuthenticationDtoResponse>> chooseRole(@RequestParam String email, @RequestParam RoleUser role, @RequestParam(required = false) String lat, @RequestParam(required = false) String lng) throws JOSEException {
        Double latitude = (lat != null && !lat.isEmpty()) ? Double.parseDouble(lat) : 0.0;
        Double longitude = (lng != null && !lng.isEmpty()) ? Double.parseDouble(lng) : 0.0;

        AuthenticationDtoResponse authenticationDtoResponse = accountService.updateAccountRole(email, role, latitude, longitude);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<AuthenticationDtoResponse>builder()
                .message("Successfully choose role for user")
                .status(200)
                .data(authenticationDtoResponse)
                .build());
    }

    @GetMapping("/is-password-set")
    public ResponseObject<Boolean> isPasswordSet(@RequestParam String email) {
        boolean isSet = accountService.isPasswordSet(email);
        return ResponseObject.<Boolean>builder()
                .message("Password status checked")
                .status(HttpStatus.OK.value())
                .data(isSet)
                .build();
    }
}
