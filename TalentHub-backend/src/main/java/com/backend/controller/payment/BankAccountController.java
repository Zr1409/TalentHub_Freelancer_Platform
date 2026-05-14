/*
package org.example.backend.controller.payment;

import lombok.RequiredArgsConstructor;
import org.example.backend.dto.ResponseObject;
import org.example.backend.dto.request.payment.BankAccountDTORequest;
import org.example.backend.dto.response.payment.BankAccountDTOResponse;
import org.example.backend.service.intf.payment.BankAccountService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/bank-accounts")
@RequiredArgsConstructor
public class BankAccountController {

    private final BankAccountService bankAccountService;

    @PostMapping
    public ResponseObject<BankAccountDTOResponse> createBankAccount(@Valid @RequestBody BankAccountDTORequest bankAccountDTORequest) {
        BankAccountDTOResponse bankAccountDTOResponse = bankAccountService.create(bankAccountDTORequest);
        return ResponseObject.<BankAccountDTOResponse>builder()
                .message("Bank Account created successfully")
                .status(HttpStatus.CREATED.value())
                .data(bankAccountDTOResponse)
                .build();
    }


    @GetMapping("/{id}")
    public ResponseObject<BankAccountDTOResponse> getBankAccountById(@PathVariable Long id) {
        return bankAccountService.getById(id)
                .map(bankAccount -> ResponseObject.<BankAccountDTOResponse>builder()
                        .message("Bank account retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(bankAccount)
                        .build())
                .orElse(ResponseObject.<BankAccountDTOResponse>builder()
                        .message("Bank account not found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @GetMapping
    public ResponseObject<List<BankAccountDTOResponse>> getAllBankAccounts() {
        List<BankAccountDTOResponse> bankAccounts = bankAccountService.getAll();
        return ResponseObject.<List<BankAccountDTOResponse>>builder()
                .message("All bank accounts retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(bankAccounts)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> deleteBankAccount(@PathVariable Long id) {
        boolean isDeleted = bankAccountService.deleteById(id);
        if (isDeleted) {
            return ResponseObject.<Void>builder()
                    .message("Bank account deleted successfully")
                    .status(HttpStatus.NO_CONTENT.value())
                    .build();
        }
        return ResponseObject.<Void>builder()
                .message("Bank account not found")
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }
}
*/
