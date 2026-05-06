package com.backend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class PaymentResDTO {
    private String status;
    private String codeVnp;
    private String message;
    private String url;
}
