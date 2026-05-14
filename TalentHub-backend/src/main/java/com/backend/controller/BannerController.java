package com.backend.controller;

import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.BannerDTORequest;
import com.backend.dto.response.BannerDTOResponse;
import com.backend.dto.response.LogoDTOResponse;
import com.backend.service.intf.BannerService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/banners")
@RequiredArgsConstructor
public class BannerController {

    private final BannerService bannerService;


    @PostMapping
    public ResponseObject<BannerDTOResponse> createBanner(
            @RequestParam String title,
            @RequestParam Double price,
            @RequestParam boolean status,
            @RequestParam String vendor,
            @RequestParam String startTime,
            @RequestParam String endTime,
            @RequestParam MultipartFile logo,
            @RequestParam MultipartFile image) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime startLocalDate;
        LocalDateTime endLocalDate;

        try {
            startLocalDate = LocalDate.parse(startTime, formatter).atStartOfDay();
            ;
            endLocalDate = LocalDate.parse(endTime, formatter).atStartOfDay();
            ;
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format. Use yyyy-MM-dd.");
        }

        BannerDTORequest bannerDTORequest = new BannerDTORequest();
        bannerDTORequest.setTitle(title);
        bannerDTORequest.setStatus(status);
        bannerDTORequest.setVendor(vendor);
        bannerDTORequest.setImage(image);
        bannerDTORequest.setLogo(logo);
        bannerDTORequest.setPrice(price);
        bannerDTORequest.setStartTime(startLocalDate);
        bannerDTORequest.setEndTime(endLocalDate);

        BannerDTOResponse createdBanner = bannerService.create(bannerDTORequest);

        return ResponseObject.<BannerDTOResponse>builder()
                .message("Banner created successfully")
                .status(HttpStatus.CREATED.value())
                .data(createdBanner)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseObject<BannerDTOResponse> updateBanner(
            @PathVariable("id") Long id,
            @RequestParam String title,
            @RequestParam boolean status,
            @RequestParam String vendor,
            @RequestParam String endTime,
            @RequestParam String startTime,
            @RequestParam Double price,
            @RequestParam(required = false) MultipartFile logo,
            @RequestParam(required = false) MultipartFile image) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        LocalDateTime startLocalDate;
        LocalDateTime endLocalDate;

        try {
            startLocalDate = LocalDate.parse(startTime, formatter).atStartOfDay();
            ;
            endLocalDate = LocalDate.parse(endTime, formatter).atStartOfDay();
            ;
        } catch (DateTimeParseException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date format. Use yyyy-MM-dd.");
        }
        BannerDTORequest bannerDTORequest = new BannerDTORequest();
        bannerDTORequest.setTitle(title);
        bannerDTORequest.setPrice(price);
        bannerDTORequest.setStatus(status);
        bannerDTORequest.setVendor(vendor);
        bannerDTORequest.setImage(image);
        bannerDTORequest.setLogo(logo);
        bannerDTORequest.setStartTime(startLocalDate);
        bannerDTORequest.setEndTime(endLocalDate);


        BannerDTOResponse updatedBanner = bannerService.update(id, bannerDTORequest);

        return ResponseObject.<BannerDTOResponse>builder()
                .message("Banner updated successfully")
                .status(HttpStatus.OK.value())
                .data(updatedBanner)
                .build();
    }

    @GetMapping("/{bannerId}")
    public ResponseObject<BannerDTOResponse> getBannerById(@PathVariable Long bannerId) {
        return bannerService.getById(bannerId)
                .map(bannerResponse -> ResponseObject.<BannerDTOResponse>builder()
                        .message("Banner retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(bannerResponse)
                        .build())
                .orElseGet(() -> ResponseObject.<BannerDTOResponse>builder()
                        .message("Banner not found with id: " + bannerId)
                        .status(HttpStatus.NOT_FOUND.value())
                        .data(null)
                        .build());
    }

    @GetMapping
    public ResponseObject<List<BannerDTOResponse>> getAllBanners() {
        List<BannerDTOResponse> banners = bannerService.getAll();
        return ResponseObject.<List<BannerDTOResponse>>builder()
                .message("All banners retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(banners)
                .build();
    }

    @GetMapping("/logos")
    public ResponseObject<List<LogoDTOResponse>> getAllLogo() {
        List<LogoDTOResponse> logos = bannerService.getAllLogo();
        return ResponseObject.<List<LogoDTOResponse>>builder()
                .message("All banners retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(logos)
                .build();
    }

    @DeleteMapping("/{bannerId}")
    public ResponseObject<Void> deleteBanner(@PathVariable Long bannerId) {
        boolean isDeleted = bannerService.deleteById(bannerId);
        if (isDeleted) {
            return ResponseObject.<Void>builder()
                    .message("Banner deleted successfully")
                    .status(HttpStatus.NO_CONTENT.value())
                    .data(null)
                    .build();
        } else {
            return ResponseObject.<Void>builder()
                    .message("Banner not found with id: " + bannerId)
                    .status(HttpStatus.NOT_FOUND.value())
                    .data(null)
                    .build();
        }
    }
}
