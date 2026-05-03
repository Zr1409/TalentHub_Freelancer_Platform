package com.backend.service.intf.payment;

import com.backend.dto.PaymentResDTO;
import com.backend.dto.request.payment.PaymentDTORequest;
import com.backend.dto.request.payment.VNPayCallbackDTORequest;
import com.backend.dto.response.ResultPaymentResponseDTO;
import com.backend.dto.response.payment.BalanceResponseDTO;
import com.backend.dto.response.payment.PaymentDTOResponse;
import com.backend.dto.response.payment.WithdrawResponseDTO;
import com.backend.service.BaseService;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

public interface PaymentService extends BaseService<PaymentDTORequest, PaymentDTOResponse, Long> {
    PaymentResDTO createVnPayPayment(BigDecimal amount) throws UnsupportedEncodingException;

    ResultPaymentResponseDTO handleVnPayCallback(VNPayCallbackDTORequest request);

    WithdrawResponseDTO handleVnPayWithCallback(BigDecimal vnpAmount, Long userId,String desc);

    BalanceResponseDTO getLatestBalanceInfo(Long userId);
}
