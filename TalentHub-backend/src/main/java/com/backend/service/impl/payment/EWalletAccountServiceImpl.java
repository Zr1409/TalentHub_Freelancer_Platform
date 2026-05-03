/*
package org.example.backend.service.impl.payment;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.request.payment.EWalletAccountDTORequest;
import org.example.backend.dto.response.payment.EWalletAccountDTOResponse;
import org.example.backend.entity.child.payment.BankAccount;
import org.example.backend.entity.child.payment.EWalletAccount;
import org.example.backend.entity.child.payment.Payment;
import org.example.backend.repository.EWalletAccountRepository;
import org.example.backend.repository.PaymentRepository;
import org.example.backend.service.intf.payment.EWalletAccountService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EWalletAccountServiceImpl implements EWalletAccountService {

    private final EWalletAccountRepository eWalletAccountRepository;
    private final PaymentRepository paymentRepository;

    @Override
    public EWalletAccountDTOResponse create(EWalletAccountDTORequest eWalletAccountDTORequest) {
        Long paymentId = eWalletAccountDTORequest.getPaymentId();


        if (paymentId != null) {
            Optional<Payment> optional = paymentRepository.findById(paymentId);
            if (!optional.isPresent()) {
                throw new IllegalArgumentException("Payment does not exist or is invalid.");
            }
            Payment object = optional.get();
            if (object.getEWalletAccount() != null) {
                throw new IllegalArgumentException("Payment is already linked to another e wallet account.");
            }
        }

        EWalletAccount eWalletAccount = new EWalletAccount();
        eWalletAccount.setEWalletName(eWalletAccountDTORequest.getEWalletName());
        eWalletAccount.setPhoneNumber(eWalletAccountDTORequest.getPhoneNumber());
        eWalletAccount.setEmail(eWalletAccountDTORequest.getEmail());
        try {
            LocalDateTime startDate = LocalDateTime.parse(eWalletAccountDTORequest.getStartDate());
            eWalletAccount.setStartDate(startDate);
        } catch (Exception e) {
            throw new IllegalArgumentException(e.getMessage());
        }

        eWalletAccount = eWalletAccountRepository.save(eWalletAccount);

        return new EWalletAccountDTOResponse(
                eWalletAccount.getId(),
                eWalletAccount.getEWalletName(),
                eWalletAccount.getPhoneNumber(),
                eWalletAccount.getEmail(),
                eWalletAccount.getStartDate()
        );
    }

    @Override
    public Optional<EWalletAccountDTOResponse> getById(Long id) {
        return eWalletAccountRepository.findById(id)
                .map(account -> new EWalletAccountDTOResponse(
                        account.getId(),
                        account.getEWalletName(),
                        account.getPhoneNumber(),
                        account.getEmail(),
                        account.getStartDate()
                ));
    }

    @Override
    public Optional<EWalletAccountDTOResponse> getByEmail(String email) {
        return eWalletAccountRepository.findByEmail(email)
                .map(account -> new EWalletAccountDTOResponse(
                        account.getId(),
                        account.getEWalletName(),
                        account.getPhoneNumber(),
                        account.getEmail(),
                        account.getStartDate()
                ));
    }

    @Override
    public Optional<EWalletAccountDTOResponse> getByPhoneNumber(String phoneNumber) {
        return eWalletAccountRepository.findByPhoneNumber(phoneNumber)
                .map(account -> new EWalletAccountDTOResponse(
                        account.getId(),
                        account.getEWalletName(),
                        account.getPhoneNumber(),
                        account.getEmail(),
                        account.getStartDate()
                ));
    }

    @Override
    public List<EWalletAccountDTOResponse> getAll() {
        return eWalletAccountRepository.findAll().stream()
                .map(account -> new EWalletAccountDTOResponse(
                        account.getId(),
                        account.getEWalletName(),
                        account.getPhoneNumber(),
                        account.getEmail(),
                        account.getStartDate()
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean deleteById(Long id) {
        if (eWalletAccountRepository.existsById(id)) {
            eWalletAccountRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
*/
