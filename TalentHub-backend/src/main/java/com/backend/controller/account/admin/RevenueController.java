package com.backend.controller.account.admin;

import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.response.account.admin.RevenueDTOResponse;
import com.backend.service.intf.account.admin.RevenueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/revenues")
@RequiredArgsConstructor
public class RevenueController {
    private final RevenueService revenueService;

    @GetMapping("/month/{year}")
    public ResponseEntity<ResponseObject<List<RevenueDTOResponse>>> getRevenueByMonth(@PathVariable int year) {
        List<RevenueDTOResponse> revenues = revenueService.getRevenueByMonth(year);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<List<RevenueDTOResponse>>builder()
                .message("Get revenue by month for year " + year + " successfully")
                .status(HttpStatus.OK.value())
                .data(revenues)
                .build());
    }

    @GetMapping("/quarter/{year}")
    public ResponseEntity<ResponseObject<List<RevenueDTOResponse>>> getRevenueByQuarter(@PathVariable int year) {
        List<RevenueDTOResponse> revenues = revenueService.getRevenueByQuater(year);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<List<RevenueDTOResponse>>builder()
                .message("Get revenue by quarter for year " + year + " successfully")
                .status(HttpStatus.OK.value())
                .data(revenues)
                .build());
    }

    /*@GetMapping("/week")
    public ResponseEntity<ResponseObject<List<RevenueDTOResponse>>> getRevenueByWeek(
            @RequestParam int year,
            @RequestParam int month) {
        List<RevenueDTOResponse> revenue = revenueService.getRevenueByWeek(year, month);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<List<RevenueDTOResponse>>builder()
                .message("Get revenue by week successfully")
                .status(200)
                .data(revenue)
                .build());
    }*/

    @GetMapping("/year")
    public ResponseEntity<ResponseObject<List<RevenueDTOResponse>>> getRevenueByYear() {
        List<RevenueDTOResponse> revenues = revenueService.getRevenueByYear();

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<List<RevenueDTOResponse>>builder()
                .message("Get revenue by year successfully")
                .status(HttpStatus.OK.value())
                .data(revenues)
                .build());
    }

    @GetMapping("/banner/month/{year}")
    public ResponseEntity<ResponseObject<List<RevenueDTOResponse>>> getRevenueBannerByMonth(@PathVariable int year) {
        List<RevenueDTOResponse> revenues = revenueService.getRevenueBannerByMonth(year);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<List<RevenueDTOResponse>>builder()
                .message("Get banner revenue by month for year " + year + " successfully")
                .status(HttpStatus.OK.value())
                .data(revenues)
                .build());
    }

    @GetMapping("/banner/quarter/{year}")
    public ResponseEntity<ResponseObject<List<RevenueDTOResponse>>> getRevenueBannerByQuater(@PathVariable int year) {
        List<RevenueDTOResponse> revenues = revenueService.getRevenueBannerByQuater(year);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<List<RevenueDTOResponse>>builder()
                .message("Get banner revenue by quarter for year " + year + " successfully")
                .status(HttpStatus.OK.value())
                .data(revenues)
                .build());
    }

    @GetMapping("/banner/year")
    public ResponseEntity<ResponseObject<List<RevenueDTOResponse>>> getRevenueBannerByYear() {
        List<RevenueDTOResponse> revenues = revenueService.getRevenueBannerByYear();

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<List<RevenueDTOResponse>>builder()
                .message("Get banner revenue by year successfully")
                .status(HttpStatus.OK.value())
                .data(revenues)
                .build());
    }

    /*@GetMapping("/banner/week")
    public ResponseEntity<ResponseObject<List<RevenueDTOResponse>>> getRevenueBannerByWeek(
            @RequestParam int year,
            @RequestParam int month) {
        List<RevenueDTOResponse> revenue = revenueService.getRevenueBannerByWeek(year, month);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<List<RevenueDTOResponse>>builder()
                .message("Get banner revenue by week successfully")
                .status(200)
                .data(revenue)
                .build());
    }*/

    @GetMapping("/growth-rate")
    public ResponseEntity<Map<String, Object>> getGrowthRate() {
        return ResponseEntity.ok(revenueService.getGrowthRates());
    }
}
