package com.backend.dto.request.account.freelancer;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateFreelancerDTORequest {
    @NotNull(message = "Hourly rate cannot be null")
    @Positive(message = "Hourly rate must be greater than 0")
    private BigDecimal hourlyRate;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description must be less than 1000 characters")
    private String description;

    @NotNull(message = "Category ID cannot be null")
    @Positive(message = "Category ID must be greater than 0")
    private Long categoryId;

    @NotNull(message = "User ID cannot be null")
    @Positive(message = "User ID must be greater than 0")
    private Long userId;
}
