package com.backend.controller.account.freelancer;

import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.account.freelancer.EducationDTORequest;
import com.backend.dto.response.account.freelancer.EducationDTOResponse;
import com.backend.service.intf.account.freelancer.EducationService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/educations")
@RequiredArgsConstructor
public class EducationController {

    private final EducationService educationService;

    @PostMapping
    public ResponseObject<EducationDTOResponse> createEducation(@Valid @RequestBody EducationDTORequest educationDTORequest) {
        EducationDTOResponse educationDTOResponse = educationService.create(educationDTORequest);
        return ResponseObject.<EducationDTOResponse>builder()
                .message("Education created successfully")
                .status(HttpStatus.CREATED.value())
                .data(educationDTOResponse)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseObject<EducationDTOResponse> getEducationById(@PathVariable Long id) {
        return educationService.getById(id)
                .map(education -> ResponseObject.<EducationDTOResponse>builder()
                        .message("Education retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(education)
                        .build())
                .orElse(ResponseObject.<EducationDTOResponse>builder()
                        .message("Education not found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @GetMapping
    public ResponseObject<List<EducationDTOResponse>> getAllEducations() {
        List<EducationDTOResponse> educations = educationService.getAll();
        return ResponseObject.<List<EducationDTOResponse>>builder()
                .message("All educations retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(educations)
                .build();
    }

    @GetMapping("/freelancer/{freelancerId}")
    public ResponseObject<List<EducationDTOResponse>> getEducationsByFreelancerId(@PathVariable Long freelancerId) {
        List<EducationDTOResponse> educations = educationService.getByFreelancerId(freelancerId);
        return ResponseObject.<List<EducationDTOResponse>>builder()
                .message("Educations for freelancer retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(educations)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseObject<EducationDTOResponse> updateEducation(@PathVariable Long id, @Valid @RequestBody EducationDTORequest educationDTORequest) {
        try {
            EducationDTOResponse educationDTOResponse = educationService.update(id, educationDTORequest);
            return ResponseObject.<EducationDTOResponse>builder()
                    .message("Education updated successfully")
                    .status(HttpStatus.OK.value())
                    .data(educationDTOResponse)
                    .build();
        } catch (Exception e) {
            return ResponseObject.<EducationDTOResponse>builder()
                    .message("Education not found or could not be updated: " + e.getMessage())
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> deleteEducation(@PathVariable Long id) {
        boolean isDeleted = educationService.deleteById(id);
        if (isDeleted) {
            return ResponseObject.<Void>builder()
                    .message("Education deleted successfully")
                    .status(HttpStatus.NO_CONTENT.value())
                    .build();
        }
        return ResponseObject.<Void>builder()
                .message("Education not found")
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }
}