package com.backend.dto.request;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class BannerDTORequest {
    private String title;
    private MultipartFile image;
    private MultipartFile logo;
    private boolean status;
    private String vendor;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double price;
}
