package com.backend.controller.account.freelancer;

import com.backend.dto.response.account.freelancer.*;
import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.account.freelancer.CreateFreelancerDTORequest;
import com.backend.dto.request.account.freelancer.FreelancerDTORequest;
import com.backend.dto.request.account.freelancer.UpdateHourlyRateDTORequest;
import com.backend.service.impl.job.JobServiceImpl;
import com.backend.service.intf.account.freelancer.FreelancerDetailService;
import com.backend.service.intf.account.freelancer.FreelancerInfoService;
import com.backend.service.intf.account.freelancer.FreelancerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/freelancers")
@RequiredArgsConstructor
public class FreelancerController {

    private final FreelancerService freelancerService;
    private final FreelancerInfoService freelancerInfoService;
    private final FreelancerDetailService freelancerDetailService;
    private final JobServiceImpl jobServiceImpl;

    @GetMapping("/client/{clientId}")
    public ResponseEntity<ResponseObject<List<FreelancerWithJobsDTOResponse>>> getFreelancersByClientId(
            @PathVariable Long clientId) {
        List<FreelancerWithJobsDTOResponse> freelancers = freelancerService.getFreelancersByClientId(clientId);
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.<List<FreelancerWithJobsDTOResponse>>builder()
                        .message("Successfully retrieved freelancers for client's open jobs")
                        .status(HttpStatus.OK.value())
                        .data(freelancers)
                        .build()
        );
    }
    @PutMapping("/{freelancerId}/category/{categoryId}")
    public ResponseEntity<ResponseObject<FreelancerDTOResponse>> updateCategory(
            @PathVariable Long freelancerId,
            @PathVariable Long categoryId) {
        FreelancerDTOResponse response = freelancerService.updateCategory(freelancerId, categoryId);
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.<FreelancerDTOResponse>builder()
                        .message("Freelancer category updated successfully")
                        .status(HttpStatus.OK.value())
                        .data(response)
                        .build()
        );
    }

    @PostMapping("")
    public ResponseObject<FreelancerDTOResponse> createFreelancer(@RequestBody FreelancerDTORequest freelancerDTORequest) {
        FreelancerDTOResponse freelancerDTOResponse = freelancerService.create(freelancerDTORequest);
        return ResponseObject.<FreelancerDTOResponse>builder()
                .message("Freelancer created successfully")
                .status(HttpStatus.CREATED.value())
                .data(freelancerDTOResponse)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseObject<FreelancerDTOResponse> getFreelancerById(@PathVariable Long id) {
        return freelancerService.getById(id)
                .map(freelancerDTOResponse -> ResponseObject.<FreelancerDTOResponse>builder()
                        .message("Freelancer retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(freelancerDTOResponse)
                        .build())
                .orElseGet(() -> ResponseObject.<FreelancerDTOResponse>builder()
                        .message("Freelancer not found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .data(null)
                        .build());
    }

    @GetMapping("")
    public ResponseObject<List<FreelancerDTOResponse>> getAllFreelancers() {
        List<FreelancerDTOResponse> freelancerDTOResponses = freelancerService.getAll();
        return ResponseObject.<List<FreelancerDTOResponse>>builder()
                .message("All freelancers retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(freelancerDTOResponses)
                .build();
    }

    @GetMapping("/admin")
    public ResponseObject<List<FreelancerAdminDTOResponse>> getAllFreelancersByAdmin() {
        List<FreelancerAdminDTOResponse> freelancerDTOResponses = freelancerService.getAllByAdmin();
        return ResponseObject.<List<FreelancerAdminDTOResponse>>builder()
                .message("All freelancers retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(freelancerDTOResponses)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> deleteFreelancerById(@PathVariable Long id) {
        return freelancerService.deleteById(id)
                ? ResponseObject.<Void>builder()
                .message("Freelancer deleted successfully")
                .status(HttpStatus.NO_CONTENT.value())
                .data(null)
                .build()
                : ResponseObject.<Void>builder()
                .message("Freelancer not found")
                .status(HttpStatus.NOT_FOUND.value())
                .data(null)
                .build();
    }

    @GetMapping("/info")
    public ResponseObject<List<FreelancerInfoDTOResponse>> getAllFreelancerInfo() {
        List<FreelancerInfoDTOResponse> freelancers = freelancerInfoService.getAllFreelancerInfo();

        return ResponseObject.<List<FreelancerInfoDTOResponse>>builder()
                .message("Get all freelancer info successfully")
                .status(HttpStatus.OK.value())
                .data(freelancers)
                .build();
    }

    @GetMapping("/detail")
    public ResponseEntity<ResponseObject<FreelancerDetailDTOResponse>> getFreelancerDetailById(@RequestParam Long id) {
        FreelancerDetailDTOResponse freelancerDetailDTOResponse = freelancerDetailService.getFreelancerDetail(id);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<FreelancerDetailDTOResponse>builder()
                .message("Successfully get detail info of freelancer")
                .status(200)
                .data(freelancerDetailDTOResponse)
                .build());
    }

    @PostMapping("/createFreelancer")
    public ResponseEntity<ResponseObject<CreateFreelancerDTOResponse>> createFreelancer(@Valid @RequestBody CreateFreelancerDTORequest createFreelancerDTORequest) {
        CreateFreelancerDTOResponse freelancerDTOResponse = freelancerService.createProfile(createFreelancerDTORequest);

        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.<CreateFreelancerDTOResponse>builder()
                        .message("Create freelancer Successfully")
                        .status(200)
                        .data(freelancerDTOResponse)
                        .build()
        );
    }

    @PostMapping("/updateHourlyRate")
    public ResponseEntity<ResponseObject<UpdateHourlyRateDTOResponse>> updateHourlyRate(@Valid @RequestBody UpdateHourlyRateDTORequest request) {
        UpdateHourlyRateDTOResponse response = freelancerService.updateHourlyRate(request);
        return ResponseEntity.status(HttpStatus.OK).body(
                ResponseObject.<UpdateHourlyRateDTOResponse>builder()
                        .message("Hourly rate updated successfully")
                        .status(HttpStatus.OK.value())
                        .data(response)
                        .build()
        );
    }

    @GetMapping("/clients/{clientId}/job-category-freelancers")
    public ResponseObject<List<FreelancerDTOResponse>> getFreelancersByClientJobCategories(@PathVariable Long clientId) {
        List<FreelancerDTOResponse> freelancers = jobServiceImpl.getFreelancersByClientJobCategories(clientId);
        return ResponseObject.<List<FreelancerDTOResponse>>builder()
                .message("Freelancers fetched successfully")
                .status(HttpStatus.OK.value())
                .data(freelancers)
                .build();
    }
}

