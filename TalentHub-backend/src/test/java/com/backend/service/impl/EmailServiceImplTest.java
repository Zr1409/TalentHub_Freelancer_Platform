package com.backend.service.impl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import com.backend.entity.child.account.PasswordResetToken;
import com.backend.enums.EmailType;
import com.backend.repository.PasswordResetTokenRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private MimeMessage mimeMessage;

    @InjectMocks
    private EmailServiceImpl emailService;

    private String testEmail;
    private String testBody;

    @BeforeEach
    void setUp() {
        testEmail = "test@example.com";
        testBody = "Test email body";
    }

    @Test
    void sendEmail_Success() throws MessagingException {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        Boolean result = emailService.sendEmail(testEmail, EmailType.OTP, testBody);

        // Assert
        assertTrue(result);
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void sendEmail_MailException_ReturnsFalse() throws MessagingException {
        // Arrange
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doThrow(new MailException("Error sending mail") {
            @Override
            public String getMessage() {
                return super.getMessage();
            }
        }).when(mailSender).send(any(MimeMessage.class));

        // Act
        Boolean result = emailService.sendEmail(testEmail, EmailType.OTP, testBody);

        // Assert
        assertFalse(result);
        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

    @Test
    void generateOtp_ReturnsValidOtp() {
        // Act
        String otp = emailService.generateOtp();

        // Assert
        assertNotNull(otp);
        assertEquals(6, otp.length());
        assertTrue(otp.matches("\\d+"));
        int otpValue = Integer.parseInt(otp);
        assertTrue(otpValue >= 100000 && otpValue <= 999999);
    }

    @Test
    void sendOtpEmail_Success() {
        // Arrange
        when(passwordResetTokenRepository.save(any(PasswordResetToken.class))).thenAnswer(i -> i.getArgument(0));
        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);
        doNothing().when(mailSender).send(any(MimeMessage.class));

        // Act
        Boolean result = emailService.sendOtpEmail(testEmail);

        // Assert
        assertTrue(result);

        // Verify OTP was saved to repository
        ArgumentCaptor<PasswordResetToken> tokenCaptor = ArgumentCaptor.forClass(PasswordResetToken.class);
        verify(passwordResetTokenRepository).save(tokenCaptor.capture());
        PasswordResetToken savedToken = tokenCaptor.getValue();

        assertEquals(testEmail, savedToken.getEmail());
        assertNotNull(savedToken.getOtp());
        assertEquals(6, savedToken.getOtp().length());
        assertTrue(savedToken.getExpiryDate().isAfter(LocalDateTime.now()));

        verify(mailSender).createMimeMessage();
        verify(mailSender).send(mimeMessage);
    }

}