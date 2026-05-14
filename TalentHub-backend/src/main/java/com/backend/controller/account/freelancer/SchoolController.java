package com.backend.controller.account.freelancer;

import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.account.freelancer.SchoolDTORequest;
import com.backend.dto.response.account.freelancer.SchoolDTOResponse;
import com.backend.service.intf.account.freelancer.SchoolService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/schools")
@RequiredArgsConstructor
public class SchoolController {

    private final SchoolService schoolService;

    @PostMapping("")
    public ResponseObject<SchoolDTOResponse> createSchool(@Valid @RequestBody SchoolDTORequest schoolDTORequest) {
        SchoolDTOResponse schoolDTOResponse = schoolService.create(schoolDTORequest);
        return ResponseObject.<SchoolDTOResponse>builder()
                .message("School created successfully")
                .status(HttpStatus.CREATED.value())
                .data(schoolDTOResponse)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseObject<SchoolDTOResponse> updateSchool(
            @PathVariable Long id,
            @Valid @RequestBody SchoolDTORequest schoolDTORequest
    ) {
        SchoolDTOResponse schoolDTOResponse = schoolService.update(id, schoolDTORequest);

        if (schoolDTOResponse == null) {
            return ResponseObject.<SchoolDTOResponse>builder()
                    .message("School not found")
                    .status(HttpStatus.NOT_FOUND.value())
                    .data(null)
                    .build();
        }

        return ResponseObject.<SchoolDTOResponse>builder()
                .message("School updated successfully")
                .status(HttpStatus.OK.value())
                .data(schoolDTOResponse)
                .build();
    }


    @GetMapping("/{id}")
    public ResponseObject<SchoolDTOResponse> getSchoolById(@PathVariable Long id) {
        return schoolService.getById(id)
                .map(school -> ResponseObject.<SchoolDTOResponse>builder()
                        .message("School retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(school)
                        .build())
                .orElse(ResponseObject.<SchoolDTOResponse>builder()
                        .message("School not found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @GetMapping("")
    public ResponseObject<List<SchoolDTOResponse>> getAllSchools() {
        List<SchoolDTOResponse> schools = schoolService.getAll();
        return ResponseObject.<List<SchoolDTOResponse>>builder()
                .message("All schools retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(schools)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> deleteSchool(@PathVariable Long id) {
        boolean isDeleted = schoolService.deleteById(id);
        if (isDeleted) {
            return ResponseObject.<Void>builder()
                    .message("School deleted successfully")
                    .status(HttpStatus.NO_CONTENT.value())
                    .build();
        }
        return ResponseObject.<Void>builder()
                .message("School not found")
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }
}
