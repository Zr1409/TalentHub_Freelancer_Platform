package com.backend.controller;

import lombok.RequiredArgsConstructor;
import com.backend.dto.PaymentResDTO;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.payment.VNPayCallbackDTORequest;
import com.backend.dto.response.ResultPaymentResponseDTO;
import com.backend.dto.response.payment.BalanceResponseDTO;
import com.backend.dto.response.payment.WithdrawResponseDTO;
import com.backend.service.intf.payment.PaymentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * Tạo thanh toán VNPAY
     */
    @GetMapping("/vnpay")
    public ResponseEntity<ResponseObject<PaymentResDTO>> vnpay(
            @RequestParam("amount") BigDecimal amount) throws UnsupportedEncodingException {
        PaymentResDTO paymentRes = paymentService.createVnPayPayment(amount);
        return ResponseEntity.ok(
                ResponseObject.<PaymentResDTO>builder()
                        .message("Tạo thanh toán thành công!")
                        .status(200)
                        .data(paymentRes)
                        .build()
        );
    }

    /**
     * Xử lý callback từ VNPAY sau khi thanh toán thành công
     */
    @PostMapping("/vnpay-callback")
    public ResponseEntity<ResponseObject<ResultPaymentResponseDTO>> vnpayCallback(
            @RequestBody VNPayCallbackDTORequest request) throws UnsupportedEncodingException {
        System.out.println("amount in controller = " + request.getVnp_Amount());

        ResultPaymentResponseDTO result = paymentService.handleVnPayCallback(request);

        return ResponseEntity.ok(
                ResponseObject.<ResultPaymentResponseDTO>builder()
                        .message("Xử lý giao dịch thành công!")
                        .status(200)
                        .data(result)
                        .build()
        );
    }


    /**
     * Xử lý rút tiền qua VNPAY
     */
    @PostMapping("/vnpay-with-callback")
    public ResponseEntity<ResponseObject<WithdrawResponseDTO>> vnpayWithCallback(
            @RequestParam("vnp_Amount") BigDecimal vnpAmount,
            @RequestParam("userId") Long userId,
            @RequestParam("desc") String desc

    ) throws UnsupportedEncodingException {
        WithdrawResponseDTO withdrawResponse = paymentService.handleVnPayWithCallback(vnpAmount, userId,desc);
        return ResponseEntity.ok(
                ResponseObject.<WithdrawResponseDTO>builder()
                        .message("Rút tiền thành công!")
                        .status(200)
                        .data(withdrawResponse)
                        .build()
        );
    }

    /**
     * Lấy thông tin số dư, lần nạp và lần tiêu gần nhất
     */
    @GetMapping("/balance")
    public ResponseEntity<ResponseObject<BalanceResponseDTO>> getLatestBalanceInfo(
            @RequestParam("userId") Long userId) {
        BalanceResponseDTO balanceInfo = paymentService.getLatestBalanceInfo(userId);
        return ResponseEntity.ok(
                ResponseObject.<BalanceResponseDTO>builder()
                        .message("Lấy thông tin số dư thành công!")
                        .status(200)
                        .data(balanceInfo)
                        .build()
        );
    }
}
