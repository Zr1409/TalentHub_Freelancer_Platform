package com.backend.config.security;

import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;

import com.backend.dto.request.account.IntrospectDTORequest;
import com.backend.enums.ErrorCode;
import com.backend.service.intf.account.AuthenticationService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {

    @Value("${jwt.secret-key}")
    protected String SECRET_KEY;

    private final AuthenticationService authenticationService;
    private NimbusJwtDecoder jwtDecoder;

    // Khởi tạo jwtDecoder một lần duy nhất
    private void initDecoder() {
        if (Objects.isNull(jwtDecoder)) {
            // Kiểm tra SECRET_KEY có hợp lệ hay không
            if (SECRET_KEY == null || SECRET_KEY.isBlank()) {
                throw new IllegalArgumentException("SECRET_KEY không được để trống!");
            }

            SecretKeySpec secretKeySpec = new SecretKeySpec(SECRET_KEY.getBytes(), "HS256");
            jwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build();
        }
    }

    @Override
    public Jwt decode(String token) throws JwtException {
        try {
            // Kiểm tra token qua service introspect
            var response = authenticationService.introspect(IntrospectDTORequest.builder()
                    .accessToken(token)
                    .build()
            );
            if (!response.getIsValid()) {
                throw new IllegalArgumentException(String.valueOf(ErrorCode.INVALID_TOKEN));
            }
        } catch (JOSEException | ParseException e) {
            throw new JwtException("Lỗi trong quá trình introspect: " + e.getMessage());
        }

        // Khởi tạo jwtDecoder nếu chưa được khởi tạo
        initDecoder();

        Jwt decodedToken;
        decodedToken = jwtDecoder.decode(token);


        return decodedToken;
    }
}
