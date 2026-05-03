package com.backend.service.intf.account;

import com.nimbusds.jose.JOSEException;
import com.backend.dto.request.account.AuthenticationDTORequest;
import com.backend.dto.request.account.IntrospectDTORequest;
import com.backend.dto.request.account.MfaLoginVerificationRequest;
import com.backend.dto.response.account.RefreshTokenDTOResponse;
import com.backend.dto.response.account.AuthenticationDtoResponse;
import com.backend.dto.response.account.IntrospectDtoResponse;

import java.text.ParseException;

public interface AuthenticationService {
    Object authenticate(AuthenticationDTORequest request) throws JOSEException;
    AuthenticationDtoResponse verifyMfaAndLogin(MfaLoginVerificationRequest request) throws JOSEException;

    IntrospectDtoResponse introspect(IntrospectDTORequest request) throws JOSEException, ParseException;

    RefreshTokenDTOResponse refreshToken(String refreshToken) throws JOSEException, ParseException;
}
