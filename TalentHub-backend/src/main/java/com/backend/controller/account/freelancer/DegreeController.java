package com.backend.controller.account.freelancer;

import lombok.RequiredArgsConstructor;

import com.backend.dto.ResponseObject;
import com.backend.dto.request.account.freelancer.DegreeDTORequest;
import com.backend.dto.response.account.freelancer.DegreeDTOResponse;
import com.backend.service.intf.account.freelancer.DegreeService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/degrees")
@RequiredArgsConstructor
public class DegreeController {

    private final DegreeService degreeService;

    @PostMapping("")
    public ResponseObject<DegreeDTOResponse> createDegree(@Valid @RequestBody DegreeDTORequest degreeDTORequest) {
        DegreeDTOResponse degreeDTOResponse = degreeService.create(degreeDTORequest);
        return ResponseObject.<DegreeDTOResponse>builder()
                .message("Degree created successfully")
                .status(HttpStatus.CREATED.value())
                .data(degreeDTOResponse)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseObject<DegreeDTOResponse> updateDegree(@PathVariable Long id, @Valid @RequestBody DegreeDTORequest degreeDTORequest) {
        DegreeDTOResponse degreeDTOResponse = degreeService.update(id, degreeDTORequest);
        return ResponseObject.<DegreeDTOResponse>builder()
                .message("Degree updated successfully")
                .status(HttpStatus.OK.value())
                .data(degreeDTOResponse)
                .build();
    }


    @GetMapping("/{id}")
    public ResponseObject<DegreeDTOResponse> getDegreeById(@PathVariable Long id) {
        return degreeService.getById(id)
                .map(degree -> ResponseObject.<DegreeDTOResponse>builder()
                        .message("Degree retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(degree)
                        .build())
                .orElse(ResponseObject.<DegreeDTOResponse>builder()
                        .message("Degree not found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @GetMapping("")
    public ResponseObject<List<DegreeDTOResponse>> getAllDegrees() {
        List<DegreeDTOResponse> degrees = degreeService.getAll();
        return ResponseObject.<List<DegreeDTOResponse>>builder()
                .message("All degrees retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(degrees)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> deleteDegree(@PathVariable Long id) {
        boolean isDeleted = degreeService.deleteById(id);
        if (isDeleted) {
            return ResponseObject.<Void>builder()
                    .message("Degree deleted successfully")
                    .status(HttpStatus.NO_CONTENT.value())
                    .build();
        }
        return ResponseObject.<Void>builder()
                .message("Degree not found")
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }
}

