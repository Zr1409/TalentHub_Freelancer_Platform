/*
package org.example.backend.mapper.payment;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.request.payment.PaymentDTORequest;
import org.example.backend.dto.response.payment.BankAccountDTOResponse;
import org.example.backend.dto.response.payment.EWalletAccountDTOResponse;
import org.example.backend.dto.response.payment.PaymentDTOResponse;
import org.example.backend.entity.child.payment.Payment;
import org.example.backend.repository.BankAccountRepository;
import org.example.backend.repository.EWalletAccountRepository;
import org.example.backend.repository.AccountRepository;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentMapper {

    private final BankAccountRepository bankAccountRepository;
    private final EWalletAccountRepository eWalletAccountRepository;
    private final AccountRepository accountRepository;

    public Payment toEntity(PaymentDTORequest paymentDTORequest) {
        if (paymentDTORequest == null) {
            return null;
        }

        Payment payment = new Payment();
        payment.setDefault(paymentDTORequest.getIsDefault());

        payment.setBankAccount(bankAccountRepository.findById(paymentDTORequest.getBankAccountId()).orElse(null));
        payment.setEWalletAccount(eWalletAccountRepository.findById(paymentDTORequest.getEWalletAccountId()).orElse(null));
        payment.setAccount(accountRepository.findById(paymentDTORequest.getAccountId()).orElse(null));

        return payment;
    }

    public PaymentDTOResponse toDTOResponse(Payment payment) {
        if (payment == null) {
            return null;
        }

        BankAccountDTOResponse bankAccountDTOResponse = null;
        if (payment.getBankAccount() != null) {
            bankAccountDTOResponse = BankAccountDTOResponse.builder()
                    .id(payment.getBankAccount().getId())
                    .bankAccountNumber(payment.getBankAccount().getBankAccountNumber())
                    .bankName(payment.getBankAccount().getBankName())
                    .startDate(payment.getBankAccount().getStartDate())
                    .status(payment.getBankAccount().getStatus())
                    .branch(payment.getBankAccount().getBranch())
                    .build();
        }

        EWalletAccountDTOResponse eWalletAccountDTOResponse = null;
        if (payment.getEWalletAccount() != null) {
            eWalletAccountDTOResponse = EWalletAccountDTOResponse.builder()
                    .id(payment.getEWalletAccount().getId())
                    .eWalletName(payment.getEWalletAccount().getEWalletName())
                    .phoneNumber(payment.getEWalletAccount().getPhoneNumber())
                    .email(payment.getEWalletAccount().getEmail())
                    .startDate(payment.getEWalletAccount().getStartDate())
                    .build();
        }

        return PaymentDTOResponse.builder()
                .id(payment.getId())
                .isDefault(payment.isDefault())
                .bankAccount(bankAccountDTOResponse)
                .eWalletAccount(eWalletAccountDTOResponse)
                .build();
    }

}
*/
