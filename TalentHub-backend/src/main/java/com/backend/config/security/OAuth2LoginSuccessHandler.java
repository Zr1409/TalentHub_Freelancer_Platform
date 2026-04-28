package com.backend.config.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import com.backend.dto.response.account.AccountDTOResponse;
import com.backend.dto.response.account.AuthenticationDtoResponse;
import com.backend.entity.child.account.Account;
import com.backend.enums.StatusAccount;
import com.backend.repository.AccountRepository;
import com.backend.service.impl.account.AccountServiceImpl;
import com.backend.service.impl.account.AuthenticationServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final AccountRepository accountRepository;
    private final AuthenticationServiceImpl authenticationService;
    private final AccountServiceImpl accountServiceImpl;
    @Value("${ui.url}")
    private String urlUI;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String registrationId = ((OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .getAuthorizedClientRegistrationId();

        String email = extractEmail(registrationId, oauthUser);

        Optional<Account> accountOpt = accountRepository.findByEmail(email);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (accountOpt.isPresent()) {
            Account account = accountOpt.get();

            if (account.getStatus() != null && account.getStatus().equals(StatusAccount.LOCKED)) {
                String redirectUrl = urlUI + "/banned-account-callback";
                response.sendRedirect(redirectUrl);
                return;
            }

            if (account.getRole() == null) {
                String redirectUrl = urlUI + "/choose-role?email=" + email;
                response.sendRedirect(redirectUrl);
                return;
            }

            try {
                AuthenticationDtoResponse authenticationDtoResponse = accountServiceImpl.handleOAuth2Login(oauthUser);

                String redirectUrl = urlUI + "/oauth2-callback?accessToken=" + authenticationDtoResponse.getAccessToken()
                        + "&role=" + authenticationDtoResponse.getRole()
                        + "&clientId=" + authenticationDtoResponse.getClientId()
                        + "&freelancerId=" + authenticationDtoResponse.getFreelancerId()
                        + "&userId=" + authenticationDtoResponse.getUserId()
                        + "&lat=" + authenticationDtoResponse.getLat()
                        + "&email=" + authenticationDtoResponse.getEmail()
                        + "&lng=" + authenticationDtoResponse.getLng();

                response.sendRedirect(redirectUrl);
            } catch (Exception e) {
                e.printStackTrace();
                String redirectUrl = urlUI + "/choose-role?email=" + email;
                response.sendRedirect(redirectUrl);
            }
        } else {
            try {
                AccountDTOResponse accountDTOResponse = accountServiceImpl.handleOAuth2Register(oauthUser);
                String redirectUrl = urlUI + "/choose-role?email=" + accountDTOResponse.getEmail();
                response.sendRedirect(redirectUrl);
            } catch (Exception e) {
                e.printStackTrace();
                String redirectUrl = urlUI + "/login?error=registration-failed";
                response.sendRedirect(redirectUrl);
            }
        }
    }

    private String extractEmail(String registrationId, OAuth2User oauthUser) {
        String email = null;

        if ("google".equals(registrationId)) {
            email = oauthUser.getAttribute("email");
        } else if ("facebook".equals(registrationId)) {
            email = oauthUser.getAttribute("id") + "@facebook.com";
        } else if ("github".equals(registrationId)) {
            email = oauthUser.getAttribute("email");

            if (email == null) {
                Object emailsObj = oauthUser.getAttribute("emails");

                if (emailsObj instanceof List) {
                    List<?> emails = (List<?>) emailsObj;
                    if (!emails.isEmpty() && emails.get(0) instanceof Map) {
                        Map<?, ?> firstEmail = (Map<?, ?>) emails.get(0);
                        email = (String) firstEmail.get("email");
                    }
                }
            }

            if (email == null) {
                String login = oauthUser.getAttribute("login");
                if (login != null) {
                    email = login + "@github.user";
                }
            }
        }

        return email;
    }
}