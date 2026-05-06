package com.backend.dto.response.account;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class IntrospectDtoResponse {
    private Boolean isValid;
}
