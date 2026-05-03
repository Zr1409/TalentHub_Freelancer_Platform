package com.backend.service.impl.account;

import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.RequiredArgsConstructor;
import com.backend.dto.request.account.AuthenticationDTORequest;
import com.backend.dto.request.account.IntrospectDTORequest;
import com.backend.dto.request.account.MfaLoginVerificationRequest;
import com.backend.dto.response.account.MfaChallengeResponse;
import com.backend.dto.response.account.RefreshTokenDTOResponse;
import com.backend.dto.response.account.AuthenticationDtoResponse;
import com.backend.dto.response.account.IntrospectDtoResponse;
import com.backend.entity.child.account.Account;
import com.backend.entity.child.account.User;
import com.backend.entity.child.account.client.Client;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.enums.RoleUser;
import com.backend.enums.StatusAccount;
import com.backend.exception.AuthenticationException;
import com.backend.exception.InvalidTokenException;
import com.backend.exception.TokenExpiredException;
import com.backend.repository.AccountRepository;
import com.backend.repository.ClientRepository;
import com.backend.repository.FreelancerRepository;
import com.backend.repository.UserRepository;
import com.backend.service.intf.account.AuthenticationService;
import org.hibernate.boot.model.naming.IllegalIdentifierException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationServiceImpl.class);
    private final AccountRepository accountRepository;
    private final UserRepository userRepository;
    private final FreelancerRepository freelancerRepository;
    private final ClientRepository clientRepository;
    private final MfaServiceImpl mfaService;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, AuthenticationContext> mfaPendingAuthentications = new HashMap<>();

    @Value("${jwt.secret-key}")
    protected String SECRET_KEY;

    private static class AuthenticationContext {
        private final String email;
        private final String tempToken;
        private final long createdAt;
        private final Double lat;
        private final Double lng;

        public AuthenticationContext(String email, String tempToken, Double lat, Double lng) {
            this.email = email;
            this.tempToken = tempToken;
            this.createdAt = System.currentTimeMillis();
            this.lat = lat;
            this.lng = lng;
        }

        public boolean isExpired() {
            // Phiên hết hạn sau 5 phút
            return System.currentTimeMillis() - createdAt > 5 * 60 * 1000;
        }
    }

    @Override
    public Object authenticate(AuthenticationDTORequest request) throws JOSEException {
        String email = request.getEmail();

        Account account = accountRepository.getByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Tài khoản không tồn tại."));

        // Bước 1: Kiểm tra mật khẩu
        if (!passwordEncoder.matches(request.getPassword(), account.getPassword())) {
            log.info("Wrong password.");
            throw new AuthenticationException("Mật khẩu không đúng.");
        }

        // Bước 2: Kiểm tra trạng thái tài khoản
        if(account.getStatus().equals(StatusAccount.LOCKED)){
            throw new AuthenticationException("Tài khoản đã bị khóa.");
        }

        // Bước 3: Kiểm tra xem tài khoản có bật 2FA không
        if (account.getMfaEnabled() != null && account.getMfaEnabled()) {
            // Nếu bật 2FA, trả về thách thức xác thực
            String tempToken = generateTempToken(account);

            // Lưu thông tin phiên đăng nhập
            mfaPendingAuthentications.put(email, new AuthenticationContext(
                    email, tempToken, request.getLat(), request.getLng()
            ));

            return MfaChallengeResponse.builder()
                    .email(email)
                    .requiresMfa(true)
                    .tempToken(tempToken)
                    .build();
        }

        // Nếu không bật 2FA, tiến hành đăng nhập bình thường
        return completeAuthentication(account, request.getLat(), request.getLng());
    }

    @Override
    public AuthenticationDtoResponse verifyMfaAndLogin(MfaLoginVerificationRequest request) throws JOSEException {
        String email = request.getEmail();
        String tempToken = request.getTempToken();
        String mfaCode = request.getMfaCode();

        // Kiểm tra xem có phiên đăng nhập đang chờ xác thực không
        AuthenticationContext context = mfaPendingAuthentications.get(email);
        if (context == null) {
            throw new AuthenticationException("Không tìm thấy phiên đăng nhập");
        }

        // Kiểm tra token tạm thời
        if (!context.tempToken.equals(tempToken)) {
            throw new AuthenticationException("Token không hợp lệ");
        }

        // Kiểm tra hết hạn
        if (context.isExpired()) {
            mfaPendingAuthentications.remove(email);
            throw new AuthenticationException("Phiên đăng nhập đã hết hạn, vui lòng đăng nhập lại");
        }

        // Kiểm tra mã 2FA
        Account account = accountRepository.getByEmail(email)
                .orElseThrow(() -> new AuthenticationException("Tài khoản không tồn tại."));

        if (!mfaService.verifyCodeDuringAuthentication(email, mfaCode)) {
            throw new AuthenticationException("Mã xác thực 2FA không đúng");
        }

        // Xóa phiên đăng nhập tạm thời
        mfaPendingAuthentications.remove(email);

        // Hoàn tất quá trình đăng nhập
        return completeAuthentication(account, context.lat, context.lng);
    }
    private String generateTempToken(Account account) throws JOSEException {
        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(account.getEmail())
                .jwtID(generateUUID())
                .issuer("talent_hubs.com")
                .claim("type", "temp") // Token tạm thời
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(5, ChronoUnit.MINUTES))) // Hết hạn sau 5 phút
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());
        JWSObject jwsObject = new JWSObject(jwsHeader, payload);
        jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));

        return jwsObject.serialize();
    }
    /**
     * Phương thức để hoàn tất quá trình đăng nhập sau khi xác thực thành công
     */
    private AuthenticationDtoResponse completeAuthentication(Account account, Double lat, Double lng) throws JOSEException {
        User user = userRepository.findById(account.getId())
                .orElseThrow(() -> new AuthenticationException("Không tìm thấy người dùng với id: " + account.getId()));

        Long userId = user.getId();
        Long freelancerId = null;
        Long clientId = null;

        if (account.getRole().equals(RoleUser.FREELANCER)) {
            Freelancer freelancer = freelancerRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new IllegalIdentifierException("Không tìm thấy freelancer với id: " + user.getId()));

            freelancerId = freelancer.getId();
        } else if (account.getRole().equals(RoleUser.CLIENT)) {
            Client client = clientRepository.findByUserId(user.getId())
                    .orElseThrow(() -> new IllegalIdentifierException("Không tìm thấy client với id: " + user.getId()));

            clientId = client.getId();
        }

        if (lat != null && lat != 0){
            account.setLat(lat);
        }
        if (lng != null && lng != 0) {
            account.setLng(lng);
        }
        Instant now = Instant.now();
        Instant expiration = now.plus(1, ChronoUnit.MINUTES); // TTL 1 phút
        long ttlSeconds = expiration.getEpochSecond() - now.getEpochSecond();

        accountRepository.save(account);

        // Trả về kết quả
        return AuthenticationDtoResponse.builder()
                .accessToken(generateAccessToken(account))
                .userId(userId)
                .freelancerId(freelancerId)
                .clientId(clientId)
                .role(account.getRole())
                .lat(account.getLat() != null ? account.getLat() : 0)
                .lng(account.getLng() != null ? account.getLng() : 0)
                .email(account.getEmail())
                .mfaEnabled(account.getMfaEnabled() != null ? account.getMfaEnabled() : false)
                .expiresIn(ttlSeconds)
                .build();
    }


    @Override
    public IntrospectDtoResponse introspect(IntrospectDTORequest request) throws JOSEException, ParseException {
        String accessToken = request.getAccessToken();
        verifyJWT(accessToken);
        return IntrospectDtoResponse.builder()
                .isValid(true)
                .build();
    }

    @Override
    public RefreshTokenDTOResponse refreshToken(String refreshToken) throws JOSEException, ParseException {
//        // Kiểm tra và xác thực refresh token
//        verifyRefreshToken(refreshToken);
//
//        // Tạo lại access token sau khi refresh
//            Account account = getAccountFromRefreshToken(refreshToken);
//        String accessToken = generateAccessToken(account);
//        return RefreshTokenDTOResponse.builder()
////                .accessToken(accessToken)
//                .build();
        return null;
    }


    private void verifyRefreshToken(String refreshToken) throws JOSEException {
        try {
            SignedJWT signedJWT = SignedJWT.parse(refreshToken);
            JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

            if (!signedJWT.verify(verifier)) {
                throw new InvalidTokenException("Refresh Token không hợp lệ.");
            }

            // Kiểm tra thời gian hết hạn
            Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
            if (expirationTime.before(new Date())) {
                throw new TokenExpiredException("Refresh Token đã hết hạn.");
            }

            // Xác minh đây là Refresh Token
            verifyTokenType(signedJWT, "refresh");
        } catch (ParseException e) {
            throw new JOSEException("Không thể parse Refresh Token.", e);
        }
    }

//    private Account getAccountFromRefreshToken(String refreshToken) throws JOSEException, ParseException {
//        // Giải mã refreshToken để lấy các claims
//        SignedJWT signedJWT = SignedJWT.parse(refreshToken);
//        JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());
//
//        // Xác minh refresh token hợp lệ (kiểm tra chữ ký và thời gian hết hạn)
//        if (!signedJWT.verify(verifier)) {
//            throw new JOSEException("Invalid refresh token signature.");
//        }
//
//        // Lấy các claim từ refresh token
//        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
//        String email = claims.getSubject();  // Subject là email người dùng, bạn có thể thay đổi tùy theo cách lưu trong token
//
//        // Lấy account từ cơ sở dữ liệu dựa trên email (hoặc thông tin khác từ token)
//        return accountRepository.getByEmail(email)
//                .orElseThrow(() -> new IllegalIdentifierException("Tài khoản không tồn tại."));
//    }
//
////    private String extractEmailFromToken(String refreshToken) throws JOSEException {
////        try {
////            SignedJWT signedJWT = SignedJWT.parse(refreshToken);
////            return signedJWT.getJWTClaimsSet().getSubject(); // Giả sử email được lưu trong subject
////        } catch (ParseException e) {
////            throw new ApplicationContextException("Không thể trích xuất email từ Refresh Token.", e);
////        }
////    }

    public String generateAccessToken(Account account) throws JOSEException {
        RoleUser roleName = account.getRole();
        String userName = account.getEmail();

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userName)
                .jwtID(generateUUID())
                .issuer("talent_hubs.com")
                .claim("type", "access") // Thêm claim "type"
                .claim("scope", roleName)
                .issueTime(new Date())
                .expirationTime(Date
                        .from(Instant.now()
                                .plus(1, ChronoUnit.HOURS))) // Ví dụ: access token hết hạn trong 1 giờ
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
        return jwsObject.serialize();
    }

    private String generateRefreshToken(Account account) throws JOSEException {
        String userName = account.getEmail();

        JWSHeader jwsHeader = new JWSHeader(JWSAlgorithm.HS256);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(userName)
                .jwtID(generateUUID())
                .issuer("talent_hubs.com")
                .claim("type", "refresh") // Thêm claim "type"
                .issueTime(new Date())
                .expirationTime(Date.from(Instant.now().plus(7, ChronoUnit.DAYS))) // Ví dụ: refresh token hết hạn trong 30 ngày
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(jwsHeader, payload);

        jwsObject.sign(new MACSigner(SECRET_KEY.getBytes()));
        return jwsObject.serialize();
    }

    private String generateUUID() {
        return UUID.randomUUID().toString();
    }
    private SignedJWT verifyJWT(String accessToken) throws ParseException, JOSEException {
        SignedJWT signedJWT = SignedJWT.parse(accessToken);
        JWSVerifier verifier = new MACVerifier(SECRET_KEY.getBytes());

        // Kiểm tra chữ ký
        if (!signedJWT.verify(verifier)) {
            throw new InvalidTokenException("Token không hợp lệ.");
        }

        // Kiểm tra thời gian hết hạn
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();
        if (expirationTime.before(new Date())) {
            throw new TokenExpiredException("Token đã hết hạn.");
        }

        // Xác minh đây là Access Token
        verifyTokenType(signedJWT, "access");

        return signedJWT;
    }

    private void verifyTokenType(SignedJWT signedJWT, String expectedType) throws ParseException, JOSEException {
        String tokenType = signedJWT.getJWTClaimsSet().getStringClaim("type");
        if (!expectedType.equals(tokenType)) {
            throw new InvalidTokenException("Token không hợp lệ cho mục đích này.");
        }
    }

}
