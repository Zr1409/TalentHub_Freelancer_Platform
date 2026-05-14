package com.backend.controller.account;

import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.account.MfaSetupRequestDTO;
import com.backend.dto.request.account.MfaVerificationRequestDTO;
import com.backend.dto.response.account.MfaSetupResponseDTO;
import com.backend.dto.response.account.MfaVerificationResponseDTO;
import com.backend.service.intf.account.MfaService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth/mfa")
public class MfaController {

    private final MfaService mfaService;

    /**
     * Setup MFA for a user
     */
    @PostMapping("/setup")
    public ResponseObject<MfaSetupResponseDTO> setupMfa(@Valid @RequestBody MfaSetupRequestDTO request) {
        MfaSetupResponseDTO response = mfaService.setupMfa(request);
        return ResponseObject.<MfaSetupResponseDTO>builder()
                .message("MFA setup successful")
                .status(200)
                .data(response)
                .build();
    }

    /**
     * Verify MFA code during setup
     */
    @PostMapping("/verify")
    public ResponseObject<MfaVerificationResponseDTO> verifyMfa(@Valid @RequestBody MfaVerificationRequestDTO request) {
        MfaVerificationResponseDTO response = mfaService.verifyCode(request);

        if (response.isVerified()) {
            // If verification is successful, enable MFA for the user
            mfaService.enableMfa(request.getEmail());
        }

        return ResponseObject.<MfaVerificationResponseDTO>builder()
                .message(response.isVerified() ? "MFA verification successful" : "MFA verification failed")
                .status(response.isVerified() ? 200 : 400)
                .data(response)
                .build();
    }

    /**
     * Disable MFA for a user
     */
    @PostMapping("/disable")
    public ResponseObject<Void> disableMfa(@RequestParam String email) {
        mfaService.disableMfa(email);
        return ResponseObject.<Void>builder()
                .message("MFA disabled successfully")
                .status(200)
                .build();
    }

    /**
     * Check if MFA is enabled for a user
     */
    @GetMapping("/status")
    public ResponseObject<Boolean> getMfaStatus(@RequestParam String email) {
        boolean isEnabled = mfaService.isMfaEnabled(email);
        return ResponseObject.<Boolean>builder()
                .message("MFA status retrieved successfully")
                .status(200)
                .data(isEnabled)
                .build();
    }
}