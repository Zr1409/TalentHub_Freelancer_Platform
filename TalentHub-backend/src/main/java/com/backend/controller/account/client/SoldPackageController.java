package com.backend.controller.account.client;

import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.account.client.SoldPackageDTORequest;
import com.backend.dto.response.account.client.CurrentPackageDTOResponse;
import com.backend.dto.response.account.client.PackageHistoryDTOResponse;
import com.backend.dto.response.account.client.SoldPackageDTOResponse;
import com.backend.service.intf.account.client.SoldPackageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/clients/soldpackages")
public class SoldPackageController {
    private final SoldPackageService soldPackageService;

    @PostMapping
    public ResponseEntity<ResponseObject<SoldPackageDTOResponse>> subcribePackage(@RequestBody SoldPackageDTORequest soldPackage) {
        SoldPackageDTOResponse soldPackageDTOResponse = soldPackageService.create(soldPackage);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseObject.<SoldPackageDTOResponse>builder()
                .message("Successfully subcribe a package")
                .status(201)
                .data(soldPackageDTOResponse)
                .build());
    }


    @GetMapping("/current/{clientId}")
    public ResponseEntity<ResponseObject<CurrentPackageDTOResponse>> getCurrentPackage(@PathVariable Long clientId) {
        Optional<CurrentPackageDTOResponse> currentPackage = soldPackageService.getCurrentPackage(clientId);
        if (currentPackage.isPresent()) {
            return ResponseEntity.ok(ResponseObject.<CurrentPackageDTOResponse>builder()
                    .message("Current package retrieved successfully")
                    .status(200)
                    .data(currentPackage.get())
                    .build());
        } else {
            return ResponseEntity.ok(ResponseObject.<CurrentPackageDTOResponse>builder()
                    .message("No active package found")
                    .status(404)
                    .build());
        }
    }

    @GetMapping("/history/{clientId}")
    public ResponseEntity<ResponseObject<List<PackageHistoryDTOResponse>>> getPackageHistory(@PathVariable Long clientId) {
        List<PackageHistoryDTOResponse> packageHistory = soldPackageService.getPackageHistory(clientId);

        return ResponseEntity.ok(ResponseObject.<List<PackageHistoryDTOResponse>>builder()
                .message("Package purchase history retrieved successfully")
                .status(200)
                .data(packageHistory)
                .build());
    }
}
