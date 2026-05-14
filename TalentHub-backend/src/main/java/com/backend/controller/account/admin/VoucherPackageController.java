package com.backend.controller.account.admin;

import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.account.admin.VoucherPackageDTORequest;
import com.backend.dto.response.account.admin.VoucherPackageDTOResponse;
import com.backend.enums.TypePackage;
import com.backend.service.intf.account.admin.VoucherPackageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/voucher-packages")
@RequiredArgsConstructor
public class VoucherPackageController {

    private final VoucherPackageService voucherPackageService;

    @PostMapping
    public ResponseObject<VoucherPackageDTOResponse> create(@RequestBody VoucherPackageDTORequest request) {
        VoucherPackageDTOResponse response = voucherPackageService.create(request);
        return ResponseObject.<VoucherPackageDTOResponse>builder()
                .message("Voucher package created successfully")
                .status(201)
                .data(response)
                .build();
    }

    @PutMapping()
    public ResponseObject<VoucherPackageDTOResponse> update(@RequestBody VoucherPackageDTORequest request) {
        VoucherPackageDTOResponse response = voucherPackageService.update(request.getTypePackage(), request);
        return ResponseObject.<VoucherPackageDTOResponse>builder()
                .message("Voucher package updated successfully")
                .status(200)
                .data(response)
                .build();
    }

    @PutMapping("/update-by-name")
    public ResponseObject<String> updateByName(@RequestParam String name, @RequestBody VoucherPackageDTORequest request) {
        String response = voucherPackageService.updateByName(name, request);
        return ResponseObject.<String>builder()
                .message("Voucher package updated successfully")
                .status(200)
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseObject<VoucherPackageDTOResponse> getById(@PathVariable Long id) {
        return voucherPackageService.getById(id)
                .map(response -> ResponseObject.<VoucherPackageDTOResponse>builder()
                        .message("Voucher package fetched successfully")
                        .status(200)
                        .data(response)
                        .build())
                .orElseGet(() -> ResponseObject.<VoucherPackageDTOResponse>builder()
                        .message("Voucher package not found")
                        .status(404)
                        .build());
    }

    @GetMapping("/type-package")
    public ResponseEntity<ResponseObject<VoucherPackageDTOResponse>> getDetailByTypePackage(@RequestParam TypePackage typePackage) {
        VoucherPackageDTOResponse response = voucherPackageService.getDetailByTypePackage(typePackage);

        return ResponseEntity.status(200).body(ResponseObject.<VoucherPackageDTOResponse>builder()
                .message("Get voucher by type successfully")
                .status(200)
                .data(response)
                .build());
    }

    @GetMapping
    public ResponseObject<List<VoucherPackageDTOResponse>> getAll() {
        List<VoucherPackageDTOResponse> response = voucherPackageService.getAll();
        return ResponseObject.<List<VoucherPackageDTOResponse>>builder()
                .message("Voucher packages fetched successfully")
                .status(200)
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> delete(@PathVariable Long id) {
        boolean deleted = voucherPackageService.deleteById(id);
        if (deleted) {
            return ResponseObject.<Void>builder()
                    .message("Voucher package deleted successfully")
                    .status(HttpStatus.NO_CONTENT.value())
                    .data(null)
                    .build();
        } else {
            return ResponseObject.<Void>builder()
                    .message("Voucher package not found")
                    .status(HttpStatus.NO_CONTENT.value())
                    .data(null)
                    .build();
        }
    }

    @GetMapping("/all-voucher")
    public ResponseEntity<ResponseObject<List<VoucherPackageDTOResponse>>> getAllVoucher() {
        List<VoucherPackageDTOResponse> response = voucherPackageService.findLatestVoucherPackagesByTypeOrdered();

        return ResponseEntity.status(200).body(ResponseObject.<List<VoucherPackageDTOResponse>>builder()
                .message("Get all voucher successfully")
                .status(200)
                .data(response)
                .build());
    }

    @GetMapping("/all-voucher/client")
    public ResponseEntity<ResponseObject<List<VoucherPackageDTOResponse>>> getAllVoucherByClient(@RequestParam Long clientId) {
        List<VoucherPackageDTOResponse> responses = voucherPackageService.findLatestVoucherPackagesByTypeByClientId(clientId);

        return ResponseEntity.status(200).body(ResponseObject.<List<VoucherPackageDTOResponse>>builder()
                .message("Get all voucher by client id successfully")
                .status(200)
                .data(responses)
                .build());
    }
}
