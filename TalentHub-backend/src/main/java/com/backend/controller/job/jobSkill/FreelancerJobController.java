package com.backend.controller.job.jobSkill;

import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.job.ClientReviewDTORequest;
import com.backend.dto.request.job.FreelancerJobDTORequest;
import com.backend.dto.request.job.FreelancerReviewDTORequest;
import com.backend.dto.request.job.ViewJobDTORequest;
import com.backend.dto.response.account.freelancer.ApplicantResponseDTO;
import com.backend.dto.response.job.ClientReviewDTOResponse;
import com.backend.dto.response.job.FreelancerJobDTOResponse;
import com.backend.dto.response.job.FreelancerReviewDTOResponse;
import com.backend.dto.response.job.ViewJobDTOResponse;
import com.backend.service.intf.job.FreelancerJobService;
import com.backend.service.intf.job.ViewJobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs")
@RequiredArgsConstructor
public class FreelancerJobController {
    private final ViewJobService viewJobService;
    private final FreelancerJobService freelancerJobService;

    @PostMapping("/view")
    public ResponseEntity<ResponseObject<ViewJobDTOResponse>> viewJob(@RequestBody ViewJobDTORequest viewJobDTORequest) {
        ViewJobDTOResponse viewJobDTOResponse = viewJobService.viewJob(viewJobDTORequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseObject.<ViewJobDTOResponse>builder()
                .message("Successfully save viewed job")
                .status(200)
                .data(viewJobDTOResponse)
                .build());
    }

    @PostMapping("/apply")
    public ResponseEntity<ResponseObject<FreelancerJobDTOResponse>> applyJob(@RequestBody FreelancerJobDTORequest freeJobDTORequest) {
        FreelancerJobDTOResponse freelancerJobDTOResponse = freelancerJobService.applyJob(freeJobDTORequest);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<FreelancerJobDTOResponse>builder()
                .message("Successfully applied for job")
                .status(200)
                .data(freelancerJobDTOResponse)
                .build());
    }

    @PostMapping("/save")
    public ResponseEntity<ResponseObject<FreelancerJobDTOResponse>> saveJob(@RequestBody FreelancerJobDTORequest freeJobDTORequest) {
        FreelancerJobDTOResponse freelancerJobDTOResponse = freelancerJobService.saveJob(freeJobDTORequest);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<FreelancerJobDTOResponse>builder()
                .message("Successfully saved for job")
                .status(200)
                .data(freelancerJobDTOResponse)
                .build());
    }

    @PostMapping("/unsave")
    public ResponseEntity<ResponseObject<FreelancerJobDTOResponse>> unSaveJob(@RequestBody FreelancerJobDTORequest freeJobDTORequest) {
        FreelancerJobDTOResponse freelancerJobDTOResponse = freelancerJobService.unSaveJob(freeJobDTORequest);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<FreelancerJobDTOResponse>builder()
                .message("Successfully unsaved for job")
                .status(200)
                .data(freelancerJobDTOResponse)
                .build());
    }

    @GetMapping("/applicants/{jobId}")
    public ResponseEntity<ResponseObject<List<ApplicantResponseDTO>>> getApplicantByJobId(@PathVariable Long jobId) {
        List<ApplicantResponseDTO> applicants = freelancerJobService.getApplicantByJobId(jobId);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<List<ApplicantResponseDTO>>builder()
                .message("Successfully get applicants")
                .status(200)
                .data(applicants)
                .build());
    }

    @PostMapping("/approve")
    public ResponseEntity<ResponseObject<FreelancerJobDTOResponse>> approveApplication(
            @RequestBody FreelancerJobDTORequest request) {
        FreelancerJobDTOResponse response = freelancerJobService.approveApplication(
                request.getJobId(),
                request.getFreelancerId()
        );

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<FreelancerJobDTOResponse>builder()
                .message("Successfully approved application")
                .status(200)
                .data(response)
                .build());
    }

    @PostMapping("/reject")
    public ResponseEntity<ResponseObject<FreelancerJobDTOResponse>> rejectApplication(
            @RequestBody FreelancerJobDTORequest request) {
        FreelancerJobDTOResponse response = freelancerJobService.rejectApplication(
                request.getJobId(),
                request.getFreelancerId()
        );

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<FreelancerJobDTOResponse>builder()
                .message("Successfully rejected application")
                .status(200)
                .data(response)
                .build());
    }

    @PostMapping("/unapply")
    public ResponseEntity<ResponseObject<FreelancerJobDTOResponse>> unapplyJob(
            @RequestBody FreelancerJobDTORequest request) {
        FreelancerJobDTOResponse response = freelancerJobService.unapplyJob(
                request.getJobId(),
                request.getFreelancerId()
        );
        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<FreelancerJobDTOResponse>builder()
                .message("Successfully unapplied from job")
                .status(200)
                .data(response)
                .build());
    }

    @PostMapping("/{id}/freelance/review")
    public ResponseEntity<ResponseObject<ClientReviewDTOResponse>> freelanceReview(@PathVariable Long id, @RequestBody ClientReviewDTORequest clientReviewDTORequest) {
        ClientReviewDTOResponse response = freelancerJobService.freelancerReview(id, clientReviewDTORequest);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<ClientReviewDTOResponse>builder()
                .message("Successfully reviewed client")
                .status(201)
                .data(response)
                .build());
    }

    @PostMapping("/{id}/client/review")
    public ResponseEntity<ResponseObject<FreelancerReviewDTOResponse>> clientReview(@PathVariable Long id, @RequestBody FreelancerReviewDTORequest freelancerReviewDTORequest) {
        FreelancerReviewDTOResponse response = freelancerJobService.clientReview(id, freelancerReviewDTORequest);

        return ResponseEntity.status(HttpStatus.OK).body(ResponseObject.<FreelancerReviewDTOResponse>builder()
                .message("Successfully reviewed client")
                .status(201)
                .data(response)
                .build());
    }
}
