package com.backend.dto.response.account.client;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UpdatePriceAndTypeDTOResponse {
    private Long clientId;
    private Double fromPrice;
    private BigDecimal toPrice;
    private String typePrice;
}
