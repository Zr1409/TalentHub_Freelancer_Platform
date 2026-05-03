package com.backend.service.intf.account;

import com.backend.dto.request.account.MfaSetupRequestDTO;
import com.backend.dto.request.account.MfaVerificationRequestDTO;
import com.backend.dto.response.account.MfaSetupResponseDTO;
import com.backend.dto.response.account.MfaVerificationResponseDTO;

public interface MfaService {
    /**
     * Setup MFA for a user
     * @param request contains email of the user
     * @return MFA setup response with secret key and QR code
     */
    MfaSetupResponseDTO setupMfa(MfaSetupRequestDTO request);

    /**
     * Verify a 2FA code provided by the user
     * @param request contains user email and 2FA code
     * @return verification result
     */
    MfaVerificationResponseDTO verifyCode(MfaVerificationRequestDTO request);

    /**
     * Enable MFA for a user after successful verification
     * @param email user's email
     */
    void enableMfa(String email);

    /**
     * Disable MFA for a user
     * @param email user's email
     */
    void disableMfa(String email);

    /**
     * Check if a user has MFA enabled
     * @param email user's email
     * @return true if MFA is enabled, false otherwise
     */
    boolean isMfaEnabled(String email);

    /**
     * Verify a code for an account during authentication
     * @param email user's email
     * @param code 2FA code
     * @return true if code is valid, false otherwise
     */
    boolean verifyCodeDuringAuthentication(String email, String code);
}