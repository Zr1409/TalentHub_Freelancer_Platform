package com.backend.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LogoDTOResponse {
    private Long id;
    private String vendor;
    private String logo;
    private boolean status;
}
