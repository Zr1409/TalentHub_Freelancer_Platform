package com.backend.service.intf;

import com.backend.enums.EmailType;

public interface EmailService {
    Boolean sendEmail(String to, EmailType emailType, String body);

    Boolean sendOtpEmail(String to);
}
