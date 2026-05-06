package com.backend.dto.request.payment;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class VNPayCallbackDTORequest {
    private String vnp_ResponseCode;
    private BigDecimal vnp_Amount;
    private Long userId;
}
