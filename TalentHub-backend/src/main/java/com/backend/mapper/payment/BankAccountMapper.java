/*
package org.example.backend.mapper.payment;

import org.example.backend.dto.request.payment.BankAccountDTORequest;
import org.example.backend.dto.response.payment.BankAccountDTOResponse;
import org.example.backend.entity.child.payment.BankAccount;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class BankAccountMapper {

    private LocalDateTime convertToLocalDateTime(String dateTimeString) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        return LocalDateTime.parse(dateTimeString, formatter);
    }

    public BankAccountDTOResponse toDTOResponse(BankAccount bankAccount) {
        if (bankAccount == null) {
            return null;
        }
        return BankAccountDTOResponse.builder()
                .id(bankAccount.getId())
                .bankName(bankAccount.getBankName())
                .bankAccountNumber(bankAccount.getBankAccountNumber())
                .branch(bankAccount.getBranch())
                .startDate(bankAccount.getStartDate())
                .status(bankAccount.getStatus())
                .build();
    }

    public BankAccount toEntity(BankAccountDTORequest bankAccountDTORequest) {
        if (bankAccountDTORequest == null) {
            return null;
        }
        BankAccount bankAccount = new BankAccount();
        bankAccount.setBankName(bankAccountDTORequest.getBankName());
        bankAccount.setBankAccountNumber(bankAccountDTORequest.getBankAccountNumber());
        bankAccount.setBranch(bankAccountDTORequest.getBranch());
        bankAccount.setStartDate(convertToLocalDateTime(bankAccountDTORequest.getStartDate().toString()));
        bankAccount.setStatus(bankAccountDTORequest.getStatus());
        return bankAccount;
    }

    public List<BankAccountDTOResponse> toDTOResponseList(List<BankAccount> bankAccounts) {
        return bankAccounts.stream()
                .map(this::toDTOResponse)
                .collect(Collectors.toList());
    }
}
*/
