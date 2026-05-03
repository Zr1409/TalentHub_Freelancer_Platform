/*
package org.example.backend.service.impl.payment;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.request.payment.BankAccountDTORequest;
import org.example.backend.dto.response.payment.BankAccountDTOResponse;
import org.example.backend.entity.child.payment.BankAccount;
import org.example.backend.entity.child.payment.Payment;
import org.example.backend.mapper.payment.BankAccountMapper;
import org.example.backend.repository.BankAccountRepository;
import org.example.backend.repository.PaymentRepository;
import org.example.backend.service.intf.payment.BankAccountService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BankAccountServiceImpl implements BankAccountService {

    private final BankAccountRepository bankAccountRepository;
    private final BankAccountMapper bankAccountMapper;
    private final PaymentRepository paymentRepository;


    @Override
    public BankAccountDTOResponse create(BankAccountDTORequest bankAccountDTORequest) {
        Long paymentId = bankAccountDTORequest.getPaymentId();


        if (paymentId != null) {
            Optional<Payment> optional = paymentRepository.findById(paymentId);
            if (!optional.isPresent()) {
                throw new IllegalArgumentException("Payment does not exist or is invalid.");
            }
            Payment object = optional.get();
            if (object.getEWalletAccount() != null) {
                throw new IllegalArgumentException("Payment is already linked to another bank account.");
            }
        }
        BankAccount bankAccount = bankAccountMapper.toEntity(bankAccountDTORequest);

        bankAccount = bankAccountRepository.save(bankAccount);

        return bankAccountMapper.toDTOResponse(bankAccount);
    }

    @Override
    public Optional<BankAccountDTOResponse> getById(Long id) {
        return bankAccountRepository.findById(id)
                .map(bankAccountMapper::toDTOResponse);
    }

    @Override
    public List<BankAccountDTOResponse> getAll() {
        return bankAccountRepository.findAll().stream()
                .map(bankAccountMapper::toDTOResponse)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean deleteById(Long id) {
        if (bankAccountRepository.existsById(id)) {
            bankAccountRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
*/
