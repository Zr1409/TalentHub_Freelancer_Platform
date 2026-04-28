package com.backend.config.security;

import com.nimbusds.jwt.SignedJWT;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.backend.exception.InvalidTokenException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {

    private final CustomJwtDecoder jwtDecoder;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String token = authorizationHeader.substring(7);

            try {
                // Decode và kiểm tra token
                SignedJWT signedJWT = SignedJWT.parse(token);
                String type = signedJWT.getJWTClaimsSet().getStringClaim("type");

                // Nếu là Refresh Token, chặn lại
                if ("refresh".equals(type) && !request.getServletPath().equals("/api/v1/auth/refresh-token")) {
                    throw new InvalidTokenException("Refresh Token không được phép gọi API này.");
                }
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                throw new InvalidTokenException("Token không hợp lệ.");
            }
        }

        filterChain.doFilter(request, response);
    }
}
