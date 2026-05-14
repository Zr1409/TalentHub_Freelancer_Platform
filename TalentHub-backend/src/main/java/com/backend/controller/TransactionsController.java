package com.backend.controller;

import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.response.payment.TransactionsDTOResponse;
import com.backend.service.intf.payment.TransactionsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/transactions")
public class TransactionsController {

    private final TransactionsService transactionsService;

    @GetMapping
    public ResponseEntity<ResponseObject<List<TransactionsDTOResponse>>> getTransactions(
            @RequestParam(value = "userId") Long userId
    ) {

        List<TransactionsDTOResponse> result = transactionsService.getTransactionsByAccountId(userId);

        return ResponseEntity.ok(
                ResponseObject.<List<TransactionsDTOResponse>>builder()
                        .message("get all giao dịch thành công!")
                        .status(200)
                        .data(result)
                        .build()
        );
    }
}
