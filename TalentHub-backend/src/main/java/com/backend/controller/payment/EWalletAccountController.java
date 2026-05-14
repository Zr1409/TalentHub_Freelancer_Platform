package com.backend.controller.payment;

/*@RestController
@RequestMapping("/api/v1/e-wallets")
@RequiredArgsConstructor
public class EWalletAccountController {

    private final EWalletAccountService eWalletAccountService;

    @PostMapping
    public ResponseObject<EWalletAccountDTOResponse> createEWalletAccount(@Valid @RequestBody EWalletAccountDTORequest eWalletAccountDTORequest) {
        EWalletAccountDTOResponse eWalletAccountDTOResponse = eWalletAccountService.create(eWalletAccountDTORequest);
        return ResponseObject.<EWalletAccountDTOResponse>builder()
                .message("E-Wallet Account created successfully")
                .status(HttpStatus.CREATED.value())
                .data(eWalletAccountDTOResponse)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseObject<EWalletAccountDTOResponse> getEWalletAccountById(@PathVariable Long id) {
        return eWalletAccountService.getById(id)
                .map(eWalletAccountDTOResponse -> ResponseObject.<EWalletAccountDTOResponse>builder()
                        .message("E-Wallet Account retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(eWalletAccountDTOResponse)
                        .build())
                .orElse(ResponseObject.<EWalletAccountDTOResponse>builder()
                        .message("E-Wallet Account not found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @GetMapping
    public ResponseObject<List<EWalletAccountDTOResponse>> getAllEWalletAccounts() {
        List<EWalletAccountDTOResponse> eWalletAccounts = eWalletAccountService.getAll();
        return ResponseObject.<List<EWalletAccountDTOResponse>>builder()
                .message("All E-Wallet Accounts retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(eWalletAccounts)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> deleteEWalletAccount(@PathVariable Long id) {
        boolean isDeleted = eWalletAccountService.deleteById(id);
        if (isDeleted) {
            return ResponseObject.<Void>builder()
                    .message("E-Wallet Account deleted successfully")
                    .status(HttpStatus.NO_CONTENT.value())
                    .build();
        }
        return ResponseObject.<Void>builder()
                .message("E-Wallet Account not found")
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }
}*/
