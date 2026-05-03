package com.backend.service.impl;

import com.backend.dto.request.account.PasswordResetTokenDTORequest;
import com.backend.dto.request.account.VerifyCodeDTORequest;
import com.backend.dto.response.account.PasswordResetTokenDTOResponse;
import com.backend.entity.child.account.Account;
import com.backend.entity.child.account.PasswordResetToken;
import com.backend.repository.AccountRepository;
import com.backend.repository.PasswordResetTokenRepository;
import com.backend.service.intf.account.PasswordResetTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PasswordResetTokenServiceImpl implements PasswordResetTokenService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public PasswordResetTokenDTOResponse resetPassword(PasswordResetTokenDTORequest passwordResetTokenDTORequest) {
        PasswordResetToken passwordResetToken = tokenRepository.findByOtpAndEmail(passwordResetTokenDTORequest.getCode(), passwordResetTokenDTORequest.getEmail());

        if (passwordResetToken == null || passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return new PasswordResetTokenDTOResponse("Token is expired", false);
        }

        Optional<Account> account = accountRepository.findByEmail(passwordResetToken.getEmail());
        if (account.isEmpty()) {
            return new PasswordResetTokenDTOResponse("Account with this email is not found", false);
        }

        Account foundAccount = account.get();
        foundAccount.setPassword(passwordEncoder.encode(passwordResetTokenDTORequest.getNewPassword()));
        accountRepository.save(foundAccount);

        tokenRepository.delete(passwordResetToken);

        return new PasswordResetTokenDTOResponse("The password is reset", true);
    }

    public PasswordResetTokenDTOResponse checkOtp(VerifyCodeDTORequest verifyCodeDTORequest) {
        PasswordResetToken passwordResetToken = tokenRepository.findByOtpAndEmail(verifyCodeDTORequest.getCode(), verifyCodeDTORequest.getEmail());
        if (passwordResetToken == null) {
            return new PasswordResetTokenDTOResponse("Invalid OTP", false);
        }

        if (passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return new PasswordResetTokenDTOResponse("OTP has expired", false);
        }

        return new PasswordResetTokenDTOResponse("OTP is valid", true);
    }
}
