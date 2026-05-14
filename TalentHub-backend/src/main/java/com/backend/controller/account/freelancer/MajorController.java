package com.backend.controller.account.freelancer;


import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.account.freelancer.MajorDTORequest;
import com.backend.dto.response.account.freelancer.MajorDTOResponse;
import com.backend.service.intf.account.freelancer.MajorService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/majors")
@RequiredArgsConstructor
public class MajorController {

    private final MajorService majorService;

    @PostMapping("")
    public ResponseObject<MajorDTOResponse> createMajor(@RequestBody MajorDTORequest majorDTORequest) {
        MajorDTOResponse majorDTOResponse = majorService.create(majorDTORequest);
        return ResponseObject.<MajorDTOResponse>builder()
                .message("Major created successfully")
                .status(HttpStatus.CREATED.value())
                .data(majorDTOResponse)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseObject<MajorDTOResponse> getMajorById(@PathVariable Long id) {
        return majorService.getById(id)
                .map(majorDTOResponse -> ResponseObject.<MajorDTOResponse>builder()
                        .message("Major retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(majorDTOResponse)
                        .build())
                .orElseGet(() -> ResponseObject.<MajorDTOResponse>builder()
                        .message("Major not found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .data(null)
                        .build());
    }

    @GetMapping("")
    public ResponseObject<List<MajorDTOResponse>> getAllMajors() {
        List<MajorDTOResponse> majorDTOResponses = majorService.getAll();
        return ResponseObject.<List<MajorDTOResponse>>builder()
                .message("All majors retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(majorDTOResponses)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> deleteMajorById(@PathVariable Long id) {
        return majorService.deleteById(id)
                ? ResponseObject.<Void>builder()
                .message("Major deleted successfully")
                .status(HttpStatus.NO_CONTENT.value())
                .data(null)
                .build()
                : ResponseObject.<Void>builder()
                .message("Major not found")
                .status(HttpStatus.NOT_FOUND.value())
                .data(null)
                .build();
    }
}
