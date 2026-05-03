package com.backend.service.intf.account;

import com.backend.dto.request.account.PasswordResetTokenDTORequest;
import com.backend.dto.request.account.VerifyCodeDTORequest;
import com.backend.dto.response.account.PasswordResetTokenDTOResponse;

public interface PasswordResetTokenService {
    PasswordResetTokenDTOResponse checkOtp(VerifyCodeDTORequest request);

    PasswordResetTokenDTOResponse resetPassword(PasswordResetTokenDTORequest passwordResetTokenDTORequest);
}
