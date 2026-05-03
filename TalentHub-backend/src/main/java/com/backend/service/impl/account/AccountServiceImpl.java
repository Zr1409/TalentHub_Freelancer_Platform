package com.backend.service.impl.account;

import com.backend.repository.*;
import com.nimbusds.jose.JOSEException;
import lombok.RequiredArgsConstructor;
import com.backend.dto.request.account.AccountDTORequest;
import com.backend.dto.request.account.AuthenticationDTORequest;
import com.backend.dto.response.account.AccountDTOResponse;
import com.backend.dto.response.account.AdminAccountDTOResponse;
import com.backend.dto.response.account.AuthenticationDtoResponse;
import com.backend.dto.response.account.LocationDTOResponse;
import com.backend.entity.child.account.Account;
import com.backend.entity.child.account.User;
import com.backend.entity.child.account.client.Client;
import com.backend.entity.child.account.client.SoldPackage;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.entity.child.admin.VoucherPackage;
import com.backend.enums.EmailType;
import com.backend.enums.RoleUser;
import com.backend.enums.StatusAccount;
import com.backend.enums.TypePackage;
import com.backend.exception.AuthenticationException;
import com.backend.exception.NotFoundException;
import com.backend.mapper.Account.AccountMapper;
import com.backend.mapper.Account.AdminAccountMapper;
import com.backend.mapper.Account.LocationMapper;
import com.backend.service.intf.EmailService;
import com.backend.service.intf.account.AccountService;
import com.backend.service.intf.account.AuthenticationService;
import com.backend.utils.GeoUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl extends SimpleUrlAuthenticationSuccessHandler implements AccountService {


    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;
    private final UserRepository userRepository;
    private final FreelancerRepository freelancerRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;
    private final AuthenticationServiceImpl authenticationServiceImpl;
    private final SoldPackageRepository soldPackageRepository;
    private final VoucherPackageRepository voucherPackageRepository;

    @Override
    public boolean changePassword(String email, String currentPassword, String newPassword) {
        Optional<Account> accountOptional = accountRepository.findByEmail(email);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();

            // Check if this is initial password setup (password is null or empty)
            boolean isInitialSetup = account.getPassword() == null || account.getPassword().trim().isEmpty();

            // If this is initial setup OR the current password matches
            if (isInitialSetup || passwordEncoder.matches(currentPassword, account.getPassword())) {
                account.setPassword(passwordEncoder.encode(newPassword));
                accountRepository.save(account);
                return true;
            }
        }
        return false;
    }
    @Override
    public Boolean checkEmail(String email) {
        return accountRepository.existsByEmail(email);
    }
    @Override
    public boolean isPasswordSet(String email) {
        Optional<Account> accountOptional = accountRepository.findByEmail(email);
        if (accountOptional.isPresent()) {
            Account account = accountOptional.get();
            String password = account.getPassword();
            return password != null && !password.trim().isEmpty();
        }
        return false;
    }
    @Transactional
    @Override
    public AccountDTOResponse create(AccountDTORequest accountRequestDTO) {
        if (accountRepository.existsByEmail(accountRequestDTO.getEmail())) {
            throw new IllegalArgumentException("Email "
                    + accountRequestDTO.getEmail() + "đã tồn tại");
        }

        Account account = accountMapper.toEntity(accountRequestDTO);
        account.setEmail(accountRequestDTO.getEmail());
        account.setPassword(passwordEncoder.encode(accountRequestDTO.getPassword()));
        account.setRole(accountRequestDTO.getRole());
        account.setStatus(accountRequestDTO.getStatus());
        System.out.println("lat " + accountRequestDTO.getLat());
        System.out.println("lng " + accountRequestDTO.getLng());

        account.setLat(accountRequestDTO.getLat());
        account.setLng(accountRequestDTO.getLng());

        Account savedAccount = accountRepository.save(account);

        User user = new User();

//        user.setFirstName(accountRequestDTO.getFirstName());
//        user.setLastName(accountRequestDTO.getLastName());
//        user.setPhoneNumber(accountRequestDTO.getPhoneNumber());
//        user.setAddress(accountRequestDTO.getAddress());
//        user.setTitle(accountRequestDTO.getTitle());
//        user.setIntroduction(accountRequestDTO.getIntroduction());

        user.setAccount(savedAccount);

//        account.setUser(user);

        accountRepository.save(account);
        userRepository.save(user);

        try {
            RoleUser role = RoleUser.valueOf(accountRequestDTO.getRole().getValue().toUpperCase());

            switch (role) {
                case FREELANCER -> {
                    Freelancer freelancer = new Freelancer();
                    freelancer.setUser(user);
                    freelancerRepository.save(freelancer);

                    emailService.sendEmail(accountRequestDTO.getEmail(), EmailType.REGISTER_SUCCESS, "Account is registered");
                }
                case CLIENT -> {
                    Client client = new Client();
                    client.setUser(user);
                    clientRepository.save(client);

                    VoucherPackage voucherPackage = voucherPackageRepository.findTopByTypePackageOrderByIdDesc(TypePackage.NORMAL);

                    SoldPackage soldPackage = new SoldPackage();
                    soldPackage.setStartDate(LocalDateTime.now());
                    soldPackage.setNumberPost(voucherPackage.getNumberPost());
                    soldPackage.setNumberPosted(Long.valueOf(0));
                    soldPackage.setVoucherPackage(voucherPackage);
                    soldPackage.setPrice(voucherPackage.getPrice());
                    soldPackage.setVoucherPackage(voucherPackage);
                    soldPackage.setClient(client);
                    soldPackage.setStatus(true);

                    soldPackageRepository.save(soldPackage);

                    emailService.sendEmail(accountRequestDTO.getFirstName(), EmailType.COMPANY_VERIFICATION_REQUEST, "");
                }
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + accountRequestDTO.getRole());
        }

        return accountMapper.toResponseDto(savedAccount);
    }

    private final AuthenticationService authenticationService;

    @Transactional
    @Override
    public AuthenticationDtoResponse register(AccountDTORequest accountRequestDTO) {
        if (accountRepository.existsByEmail(accountRequestDTO.getEmail())) {
            throw new AuthenticationException("Email "
                    + accountRequestDTO.getEmail() + " đã được đăng ký");
        }

        Account account = accountMapper.toEntity(accountRequestDTO);
        account.setEmail(accountRequestDTO.getEmail());
        account.setPassword(passwordEncoder.encode(accountRequestDTO.getPassword()));
        account.setRole(accountRequestDTO.getRole());
        account.setStatus(accountRequestDTO.getStatus());
        System.out.println("lat " + accountRequestDTO.getLat());
        System.out.println("lng " + accountRequestDTO.getLng());

        account.setLat(accountRequestDTO.getLat());
        account.setLng(accountRequestDTO.getLng());

        Account savedAccount = accountRepository.save(account);

        User user = new User();

        user.setFirstName(accountRequestDTO.getFirstName());
        user.setLastName(accountRequestDTO.getLastName());
        user.setPhoneNumber(accountRequestDTO.getPhoneNumber());
        user.setWard(accountRequestDTO.getWard());
        user.setProvince(accountRequestDTO.getProvince());
        user.setTitle(accountRequestDTO.getTitle());
        user.setIntroduction(accountRequestDTO.getIntroduction());

        user.setAccount(savedAccount);

        account.setUser(user);

        accountRepository.save(account);
        userRepository.save(user);

        try {
            RoleUser role = RoleUser.valueOf(accountRequestDTO.getRole().getValue().toUpperCase());

            switch (role) {
                case FREELANCER -> {
                    Freelancer freelancer = new Freelancer();
                    freelancer.setUser(user);
                    freelancerRepository.save(freelancer);

                    emailService.sendEmail(accountRequestDTO.getFirstName(), EmailType.REGISTER_SUCCESS, "Account is registered");
                }
                case CLIENT -> {
                    Client client = new Client();
                    client.setUser(user);
                    clientRepository.save(client);

                    VoucherPackage voucherPackage = voucherPackageRepository.findTopByTypePackageOrderByIdDesc(TypePackage.NORMAL);

                    SoldPackage soldPackage = new SoldPackage();
                    soldPackage.setStartDate(LocalDateTime.now());
                    soldPackage.setNumberPost(voucherPackage.getNumberPost());
                    soldPackage.setNumberPosted(Long.valueOf(0));
                    soldPackage.setVoucherPackage(voucherPackage);
                    soldPackage.setPrice(voucherPackage.getPrice());
                    soldPackage.setVoucherPackage(voucherPackage);
                    soldPackage.setClient(client);
                    soldPackage.setStatus(true);

                    soldPackageRepository.save(soldPackage);

                    emailService.sendEmail(accountRequestDTO.getFirstName(), EmailType.COMPANY_VERIFICATION_REQUEST, "");
                }
            }
        } catch (IllegalArgumentException e) {
            throw new AuthenticationException("Invalid role: " + accountRequestDTO.getRole());
        }

        try {
            AuthenticationDTORequest authRequest = AuthenticationDTORequest.builder()
                    .email(account.getEmail())
                    .lat(account.getLat())
                    .lng(account.getLng())
                    .password(accountRequestDTO.getPassword())
                    .build();

            Object authResult = authenticationService.authenticate(authRequest);

            if (authResult instanceof AuthenticationDtoResponse) {
                return (AuthenticationDtoResponse) authResult;
            } else {
                return AuthenticationDtoResponse.builder()
                        .accessToken(authenticationServiceImpl.generateAccessToken(account))
                        .userId(user.getId())
                        .role(account.getRole())
                        .email(account.getEmail())
                        .lat(account.getLat() != null ? account.getLat() : 0)
                        .lng(account.getLng() != null ? account.getLng() : 0)
                        .mfaEnabled(account.getMfaEnabled() != null ? account.getMfaEnabled() : false)
                        .build();
            }
        } catch (JOSEException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Optional<AccountDTOResponse> getById(Long id) {
        return Optional.ofNullable(accountRepository.findById(id)
                .map(accountMapper::toResponseDto)
                .orElseThrow(() -> new NotFoundException("Account with ID " + id + " not found")));
    }

    @Override
    public List<AccountDTOResponse> getAll() {
        List<Account> accounts = accountRepository.findAll();

        if (accounts.isEmpty()) {
            throw new NotFoundException("No accounts found");
        }
        return accountMapper.toResponseDtoList(accounts);
    }

    private final AdminAccountMapper adminAccountMapper;

    @Override
    public List<AdminAccountDTOResponse> getAllByAdmin() {
        List<Account> accounts = accountRepository.findAll();


        return accounts.stream().map(adminAccountMapper::toResponseDTO).toList();
    }

    @Override
    public Boolean deleteById(Long id) {
        return null;
    }

    @Transactional
    public AuthenticationDtoResponse handleOAuth2Login(OAuth2User oauthUser) throws JOSEException {
        String registrationId = ((OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .getAuthorizedClientRegistrationId();
        System.out.println("OAuth2 provider: " + registrationId);

        String email = null;

        if ("google".equals(registrationId)) {
            email = oauthUser.getAttribute("email");
        } else if ("facebook".equals(registrationId)) {
            email = oauthUser.getAttribute("id") + "@facebook.com";
        } else if ("github".equals(registrationId)) {
            // Try to get email directly
            email = oauthUser.getAttribute("email");

            // If null, try to get from emails list
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

            // If still null, use login name
            if (email == null) {
                String login = oauthUser.getAttribute("login");
                if (login != null) {
                    email = login + "@github.user";
                }
            }
        }

        // Try to find the account, if not found, register it first
        Optional<Account> accountOpt = accountRepository.findByEmail(email);
        if (!accountOpt.isPresent()) {
            // If account doesn't exist, create it first
            AccountDTOResponse newAccountResponse = handleOAuth2Register(oauthUser);
            accountOpt = accountRepository.findByEmail(newAccountResponse.getEmail());

            if (!accountOpt.isPresent()) {
                throw new IllegalArgumentException("Failed to create account during OAuth2 login");
            }
        }

        Account account = accountOpt.get();
        User user = userRepository.findById(account.getId())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy user với account này"));

        Long freelancerId = null;
        Long clientId = null;

        // Only check for freelancer/client if role is set
        if (account.getRole() != null) {
            if (account.getRole().equals(RoleUser.FREELANCER)) {
                Optional<Freelancer> freelancerOpt = freelancerRepository.findByUserId(user.getId());
                if (freelancerOpt.isPresent()) {
                    freelancerId = freelancerOpt.get().getId();
                }
            } else if (account.getRole().equals(RoleUser.CLIENT)) {
                Optional<Client> clientOpt = clientRepository.findByUserId(user.getId());
                if (clientOpt.isPresent()) {
                    clientId = clientOpt.get().getId();
                }
            }
        }

        return AuthenticationDtoResponse.builder()
                .accessToken(authenticationServiceImpl.generateAccessToken(account))
                .userId(user.getId())
                .freelancerId(freelancerId)
                .clientId(clientId)
                .role(account.getRole())
                .lat(account.getLat() != null ? account.getLat() : 0)
                .email(account.getEmail())
                .lng(account.getLng() != null ? account.getLng() : 0)
                .build();
    }

    public AccountDTOResponse handleOAuth2Register(OAuth2User oauthUser) {
        System.out.println("Đăng ký người dùng OAuth2 " + oauthUser.toString());

        String registrationId = ((OAuth2AuthenticationToken) SecurityContextHolder.getContext().getAuthentication())
                .getAuthorizedClientRegistrationId();
        System.out.println("Nhà cung cấp OAuth2 cho đăng ký: " + registrationId);

        String email = null;
        String firstName = null;
        String lastName = null;
        String pictureUrl = null;

        if ("google".equals(registrationId)) {
            email = oauthUser.getAttribute("email");
            firstName = oauthUser.getAttribute("given_name");
            lastName = oauthUser.getAttribute("family_name");
            pictureUrl = oauthUser.getAttribute("picture");
            System.out.println("Đăng ký Google, sử dụng email: " + email);
        } else if ("facebook".equals(registrationId)) {
            String fullName = oauthUser.getAttribute("name");
            if (fullName != null && fullName.contains(" ")) {
                String[] nameParts = fullName.split(" ");
                firstName = nameParts[0];
                lastName = nameParts.length > 1 ? nameParts[1] : "";
            } else {
                firstName = fullName;
                lastName = "";
            }
            email = oauthUser.getAttribute("id") + "@facebook.com";
            Map<String, Object> pictureData = oauthUser.getAttribute("picture");
            if (pictureData != null && pictureData.containsKey("data")) {
                Map<String, Object> data = (Map<String, Object>) pictureData.get("data");
                if (data.containsKey("url")) {
                    pictureUrl = (String) data.get("url");
                }
            }
            System.out.println("Đăng ký Facebook, sử dụng ID Facebook làm email: " + email);
        } else if ("github".equals(registrationId)) {
            // Lấy email từ GitHub
            email = oauthUser.getAttribute("email");
            System.out.println("GitHub email trực tiếp: " + email);

            // Nếu email null, dùng login name
            if (email == null) {
                String login = oauthUser.getAttribute("login");
                if (login != null) {
                    email = login + "@github.user";
                    System.out.println("Sử dụng tên đăng nhập GitHub làm email: " + email);
                }
            }

            // Lấy tên đầy đủ và tách thành first name và last name
            String fullName = oauthUser.getAttribute("name");
            if (fullName != null && fullName.contains(" ")) {
                String[] nameParts = fullName.split(" ", 2);
                firstName = nameParts[0];
                lastName = nameParts[1];
            } else {
                firstName = fullName != null ? fullName : oauthUser.getAttribute("login");
                lastName = "";
            }

            // Lấy URL hình ảnh
            pictureUrl = oauthUser.getAttribute("avatar_url");
            System.out.println("Picture URL: " + pictureUrl);

            System.out.println("Đăng ký GitHub, sử dụng email: " + email);
        }

        // Kiểm tra email có hợp lệ không
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Không thể lấy email từ tài khoản OAuth2");
        }

        // Kiểm tra tài khoản đã tồn tại chưa
        Optional<Account> existingAccount = accountRepository.findByEmail(email);
        if (existingAccount.isPresent()) {
            System.out.println("Tài khoản với email " + email + " đã tồn tại, trả về thông tin hiện có");
            return accountMapper.toResponseDto(existingAccount.get());
        }

        // Tạo tài khoản mới
        System.out.println("Tạo tài khoản mới với email: " + email);
        Account newAccount = new Account();
        newAccount.setEmail(email);
        newAccount.setLng(0.0);
        newAccount.setLat(0.0);
        newAccount.setStatus(StatusAccount.ACTIVE);
        newAccount.setIsOAuthAccount(true);

        // Role chưa được thiết lập ở đây, người dùng sẽ chọn role sau
        accountRepository.save(newAccount);

        User user = new User();
        user.setAccount(newAccount);
        user.setImage(pictureUrl);
        // Đảm bảo firstName không null
        user.setFirstName(firstName != null ? firstName : "User");
        user.setLastName(lastName != null ? lastName : "");
        userRepository.save(user);

        newAccount.setUser(user);
        accountRepository.save(newAccount);

        System.out.println("Đã tạo tài khoản mới thành công với ID: " + newAccount.getId());
        return accountMapper.toResponseDto(newAccount);
    }
    @Override
    public AuthenticationDtoResponse updateAccountRole(String email, RoleUser role, double lat, double lng) throws JOSEException {
        AuthenticationDtoResponse authenticationDtoResponse = new AuthenticationDtoResponse();
        Optional<Account> account = accountRepository.findByEmail(email);
        User user = userRepository.findByAccount_Id(account.get().getId());
        account.get().setRole(role);
        account.get().setLat(lat);
        account.get().setLng(lng);
        accountRepository.save(account.get());

        try {
            switch (role) {
                case FREELANCER -> {
                    Freelancer freelancer = new Freelancer();
                    freelancer.setUser(user);
                    freelancerRepository.save(freelancer);
                    authenticationDtoResponse.setFreelancerId(freelancer.getId());

                    account.get().setStatus(StatusAccount.ACTIVE);
                    accountRepository.save(account.get());

                    emailService.sendEmail(email, EmailType.REGISTER_SUCCESS, "Account is registered");
                }
                case CLIENT -> {
                    Client client = new Client();
                    client.setUser(user);
                    clientRepository.save(client);
                    authenticationDtoResponse.setClientId(client.getId());

                    if (account.get().getIsOAuthAccount() != null && account.get().getIsOAuthAccount()) {
                        account.get().setStatus(StatusAccount.ACTIVE);
                    } else {
                        account.get().setStatus(StatusAccount.ACTIVE);
                    }
                    accountRepository.save(account.get());

                    VoucherPackage voucherPackage = voucherPackageRepository.findTopByTypePackageOrderByIdDesc(TypePackage.NORMAL);

                    SoldPackage soldPackage = new SoldPackage();
                    soldPackage.setStartDate(LocalDateTime.now());
                    soldPackage.setNumberPost(voucherPackage.getNumberPost());
                    soldPackage.setNumberPosted(Long.valueOf(0));
                    soldPackage.setVoucherPackage(voucherPackage);
                    soldPackage.setPrice(voucherPackage.getPrice());
                    soldPackage.setVoucherPackage(voucherPackage);
                    soldPackage.setClient(client);
                    soldPackage.setStatus(true);

                    soldPackageRepository.save(soldPackage);

                    emailService.sendEmail(user.getFirstName(), EmailType.COMPANY_VERIFICATION_REQUEST, "");
                }
            }
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid role: " + account.get().getRole());
        }

        return AuthenticationDtoResponse.builder()
                .accessToken(authenticationServiceImpl.generateAccessToken(account.get()))
                .userId(user.getId())
                .freelancerId(authenticationDtoResponse.getFreelancerId())
                .clientId(authenticationDtoResponse.getClientId())
                .role(account.get().getRole())
                .lat(lat)
                .email(account.get().getEmail())
                .lng(lng)
                .build();
    }

    @Override
    public List<AccountDTOResponse> getNearbyUsers(double lat, double lon, double distanceInMeters) {
        List<Account> accounts = accountRepository.findAll();

        List<AccountDTOResponse> nearbyUsers = accounts.stream()
                .filter(account -> {
                    double distance = GeoUtils.calculateDistance(lat, lon, account.getLat(), account.getLng());
                    return distance <= distanceInMeters;
                })
                .map(accountMapper::toResponseDto)
                .collect(Collectors.toList());

        return nearbyUsers;
    }

    @Override
    public Boolean banAccount(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isPresent()) {
            Account foundAccount = account.get();
            foundAccount.setStatus(StatusAccount.LOCKED);
            accountRepository.save(foundAccount);
            return true;
        }
        return false;
    }

    @Override
    public Boolean unBanAccount(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isPresent()) {
            Account foundAccount = account.get();
            foundAccount.setStatus(StatusAccount.ACTIVE);
            accountRepository.save(foundAccount);
            return true;
        }

        return false;
    }

    @Override
    public Boolean activeAccount(String email) {
        Optional<Account> account = accountRepository.findByEmail(email);
        if (account.isPresent()) {
            Account foundAccount = account.get();
            foundAccount.setStatus(StatusAccount.ACTIVE);
            accountRepository.save(foundAccount);
            return true;
        }
        return false;
    }


    private final LocationMapper locationMapper;

    @Override
    public List<LocationDTOResponse> getLocations() {
        // Lấy tất cả các user có account
        List<User> usersWithAccount = userRepository.findAll().stream()
                .filter(user -> user.getAccount() != null)
                .collect(Collectors.toList());

        // Tạo danh sách LocationDTOResponse từ thông tin user và account
        return usersWithAccount.stream()
                .map(user -> {
                    Account account = user.getAccount();
                    String address = "";

                    // Tạo address từ ward và province
                    if (user.getWard() != null && !user.getWard().isEmpty()) {
                        address += user.getWard();
                        if (user.getProvince() != null && !user.getProvince().isEmpty()) {
                            address += ", " + user.getProvince();
                        }
                    } else if (user.getProvince() != null && !user.getProvince().isEmpty()) {
                        address = user.getProvince();
                    } else {
                        address = "Email: " + account.getEmail();
                    }

                    return LocationDTOResponse.builder()
                            .address(address)
                            .lat(account.getLat())
                            .lng(account.getLng())
                            .role(account.getRole())
                            .build();
                })
                .toList();
    }
}
