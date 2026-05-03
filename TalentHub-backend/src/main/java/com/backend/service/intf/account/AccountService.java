package com.backend.service.intf.account;

import com.nimbusds.jose.JOSEException;
import com.backend.dto.request.account.AccountDTORequest;
import com.backend.dto.response.account.AccountDTOResponse;
import com.backend.dto.response.account.AdminAccountDTOResponse;
import com.backend.dto.response.account.AuthenticationDtoResponse;
import com.backend.dto.response.account.LocationDTOResponse;
import com.backend.enums.RoleUser;
import com.backend.service.BaseService;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.List;

public interface AccountService extends BaseService<AccountDTORequest, AccountDTOResponse, Long> {
    public AccountDTOResponse handleOAuth2Register(OAuth2User oauthUser);

    public AuthenticationDtoResponse handleOAuth2Login(OAuth2User oauthUser) throws JOSEException;

    public AuthenticationDtoResponse updateAccountRole(String email, RoleUser role, double lat, double lng) throws JOSEException;

    Boolean checkEmail(String email);
    boolean isPasswordSet(String email);

    public List<AdminAccountDTOResponse> getAllByAdmin();

    public List<AccountDTOResponse> getNearbyUsers(double lat, double lon, double distanceInMeters);

    Boolean banAccount(String email);

    Boolean unBanAccount(String email);

    Boolean activeAccount(String email);

    List<LocationDTOResponse> getLocations();

    boolean changePassword(String email, String currentPassword, String newPassword);

    public AuthenticationDtoResponse register(AccountDTORequest accountRequestDTO);
}

