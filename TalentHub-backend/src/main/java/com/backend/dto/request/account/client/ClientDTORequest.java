package com.backend.dto.request.account.client;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDTORequest {

    @NotNull(message = "From Price is required")
    private Double fromPrice;

    @NotNull(message = "To Price is required")
    private Double toPrice;

    private String typePrice;

    @NotNull(message = "User ID is required")
    private Long userId;
}
