package com.backend.dto.request.job;

import jakarta.validation.constraints.*;
import lombok.*;
import com.backend.enums.StatusJob;
import com.backend.enums.TypePayment;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@Builder
public class CreateJobDTORequest {
    @NotBlank(message = "Title cannot be blank")
    @Size(max = 255, message = "Title must be less than 255 characters")
    private String title;

    @NotBlank(message = "Description cannot be blank")
    private String description;

    @NotBlank(message = "Scope cannot be blank")
    private String scope;

    @NotNull(message = "Hour work cannot be null")
    @Positive(message = "Hour work must be greater than 0")
    private BigDecimal hourWork;

    @NotNull(message = "Duration cannot be null")
    @Positive(message = "Duration must be greater than 0")
    private Long duration;

    @NotNull(message = "Job opportunity cannot be null")
    private Boolean jobOpportunity;

    @NotNull(message = "From price cannot be null")
    @PositiveOrZero(message = "From price must be 0 or greater")
    private BigDecimal fromPrice;

    @NotNull(message = "To price cannot be null")
    @PositiveOrZero(message = "To price must be 0 or greater")
    private BigDecimal toPrice;

    @NotBlank(message = "Type price cannot be blank")
    private String typePrice;

    @NotNull(message = "Type payment cannot be null")
    private TypePayment typePayment;

    @NotNull(message = "Status job cannot be null")
    private StatusJob statusJob;

    @NotNull(message = "Client ID cannot be null")
    @Positive(message = "Client ID must be greater than 0")
    private Long clientId;

    @NotNull(message = "Category ID cannot be null")
    @Positive(message = "Category ID must be greater than 0")
    private Long categoryId;

    @NotNull(message = "Skill ID cannot be null")
    private List<Long> skillId;
}
