package com.backend.service.impl.account;

import dev.samstevens.totp.code.*;
import dev.samstevens.totp.exceptions.QrGenerationException;
import dev.samstevens.totp.qr.QrData;
import dev.samstevens.totp.qr.QrGenerator;
import dev.samstevens.totp.qr.ZxingPngQrGenerator;
import dev.samstevens.totp.secret.DefaultSecretGenerator;
import dev.samstevens.totp.secret.SecretGenerator;
import dev.samstevens.totp.time.SystemTimeProvider;
import dev.samstevens.totp.time.TimeProvider;
import lombok.RequiredArgsConstructor;
import com.backend.dto.request.account.MfaSetupRequestDTO;
import com.backend.dto.request.account.MfaVerificationRequestDTO;
import com.backend.dto.response.account.MfaSetupResponseDTO;
import com.backend.dto.response.account.MfaVerificationResponseDTO;
import com.backend.entity.child.account.Account;
import com.backend.exception.AuthenticationException;
import com.backend.repository.AccountRepository;
import com.backend.service.intf.account.MfaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static dev.samstevens.totp.util.Utils.getDataUriForImage;

@Service
@RequiredArgsConstructor
public class MfaServiceImpl implements MfaService {

    private static final Logger log = LoggerFactory.getLogger(MfaServiceImpl.class);
    private final AccountRepository accountRepository;

    private static final String ISSUER = "TalentHub";
    private static final int CODE_DIGITS = 6;
    private static final int PERIOD = 30;
    private static final int VALIDATION_WINDOW = 1;

    @Override
    public MfaSetupResponseDTO setupMfa(MfaSetupRequestDTO request) {
        // Get user account
        Account account = accountRepository.getByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("Tài khoản không tồn tại."));

        // Generate a secret key
        SecretGenerator secretGenerator = new DefaultSecretGenerator();
        String secretKey = secretGenerator.generate();

        // Store the secret key (but don't enable MFA yet)
        account.setMfaSecret(secretKey);
        account.setMfaEnabled(false);
        accountRepository.save(account);

        // Generate QR code data
        QrData qrData = new QrData.Builder()
                .label(account.getEmail())
                .secret(secretKey)
                .issuer(ISSUER)
                .algorithm(HashingAlgorithm.SHA1)
                .digits(CODE_DIGITS)
                .period(PERIOD)
                .build();

        // Generate QR code image
        QrGenerator qrGenerator = new ZxingPngQrGenerator();
        byte[] qrCodeImage;
        try {
            qrCodeImage = qrGenerator.generate(qrData);
        } catch (QrGenerationException e) {
            log.error("Error generating QR code", e);
            throw new RuntimeException("Không thể tạo mã QR cho MFA.");
        }

        // Convert QR code to data URI
        String qrCodeImageUri = getDataUriForImage(qrCodeImage, qrGenerator.getImageMimeType());

        // Return response
        return MfaSetupResponseDTO.builder()
                .secretKey(secretKey)
                .qrCodeImage(qrCodeImageUri)
                .build();
    }

    @Override
    public MfaVerificationResponseDTO verifyCode(MfaVerificationRequestDTO request) {
        Account account = accountRepository.getByEmail(request.getEmail())
                .orElseThrow(() -> new AuthenticationException("Tài khoản không tồn tại."));

        // Verify the code
        boolean isValid = verifyCode(account.getMfaSecret(), request.getCode());

        return MfaVerificationResponseDTO.builder()
                .verified(isValid)
                .build();
    }

    @Override
    public void enableMfa(String email) {
        Account account = accountRepository.getByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Tài khoản không tồn tại."));

        account.setMfaEnabled(true);
        accountRepository.save(account);
    }

    @Override
    public void disableMfa(String email) {
        Account account = accountRepository.getByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Tài khoản không tồn tại."));

        account.setMfaEnabled(false);
        account.setMfaSecret(null);
        accountRepository.save(account);
    }

    @Override
    public boolean isMfaEnabled(String email) {
        Account account = accountRepository.getByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Tài khoản không tồn tại."));

        return account.getMfaEnabled() != null && account.getMfaEnabled();
    }

    @Override
    public boolean verifyCodeDuringAuthentication(String email, String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }

        Account account = accountRepository.getByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Tài khoản không tồn tại."));

        if (account.getMfaEnabled() == null || !account.getMfaEnabled()) {
            // MFA not enabled, so no verification needed
            return true;
        }

        return verifyCode(account.getMfaSecret(), code);
    }

    private boolean verifyCode(String secret, String code) {
        if (code == null || code.trim().isEmpty()) {
            return false;
        }

        TimeProvider timeProvider = new SystemTimeProvider();
        CodeGenerator codeGenerator = new DefaultCodeGenerator();
        CodeVerifier verifier = new DefaultCodeVerifier(codeGenerator, timeProvider);

        // Allow codes from the previous time window (helps with timing issues)
        ((DefaultCodeVerifier) verifier).setTimePeriod(PERIOD);
        ((DefaultCodeVerifier) verifier).setAllowedTimePeriodDiscrepancy(VALIDATION_WINDOW);

        return verifier.isValidCode(secret, code);
    }
}