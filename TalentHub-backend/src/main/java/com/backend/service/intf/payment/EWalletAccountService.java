package com.backend.service.intf.payment;

import com.backend.dto.request.payment.EWalletAccountDTORequest;
import com.backend.dto.response.payment.EWalletAccountDTOResponse;
import com.backend.service.BaseService;

import java.util.Optional;

public interface EWalletAccountService extends BaseService<EWalletAccountDTORequest, EWalletAccountDTOResponse, Long> {
    Optional<EWalletAccountDTOResponse> getByPhoneNumber(String phoneNumber);

    Optional<EWalletAccountDTOResponse> getByEmail(String email);

}
