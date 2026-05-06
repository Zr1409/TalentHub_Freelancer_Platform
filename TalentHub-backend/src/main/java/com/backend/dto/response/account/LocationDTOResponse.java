package com.backend.dto.response.account;

import lombok.*;
import com.backend.enums.RoleUser;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LocationDTOResponse {
    private String address;
    private Double lat;
    private Double lng;
    private RoleUser role;
}
