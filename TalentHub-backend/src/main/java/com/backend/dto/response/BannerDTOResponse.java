package com.backend.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class BannerDTOResponse {
    private Long id;
    private String title;
    private String image;
    private String logo;
    private boolean status;
    private String vendor;
    private Double price;
//    private String createdAt;
//    private String updatedAt;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}
