package com.backend.dto.request.account.client;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
public class UpdatePriceAndTypeDTORequest {
    @NotNull(message = "Client ID cannot be null")
    private Long clientId;

    @NotNull(message = "From price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "From price must be greater than 0")
    private Double fromPrice;

    @NotNull(message = "To price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "To price must be greater than 0")
    private Double toPrice;

    @NotNull(message = "Type price cannot be null")
    @Size(min = 1, max = 50, message = "Type price must be between 1 and 50 characters")
    private String typePrice;
}