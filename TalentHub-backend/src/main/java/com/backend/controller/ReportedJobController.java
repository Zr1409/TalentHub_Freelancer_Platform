package com.backend.controller;

import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.ReportedJobDTORequest;
import com.backend.dto.response.ReportedJobDTOResponse;
import com.backend.enums.ReportedJobStatus;
import com.backend.service.intf.ReportedJobService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/reported-jobs")
@RequiredArgsConstructor
public class ReportedJobController {

    private final ReportedJobService reportedJobService;

    @PostMapping
    public ResponseObject<ReportedJobDTOResponse> create(
            @RequestParam String reasonFreelancer,
            @RequestParam String reasonAdmin,
            @RequestParam String description,
//            @RequestParam ReportedJobStatus status,
            @RequestParam Long freelancerId,
            @RequestParam Long jobId,
            @RequestParam MultipartFile image) {

        ReportedJobDTORequest reportedJobDTORequest = new ReportedJobDTORequest();
        reportedJobDTORequest.setReasonFreelancer(reasonFreelancer);
        reportedJobDTORequest.setReasonAdmin(reasonAdmin);
        reportedJobDTORequest.setDescription(description);
        reportedJobDTORequest.setStatus(ReportedJobStatus.IN_PROGRESS);
        reportedJobDTORequest.setFreelancerId(freelancerId);
        reportedJobDTORequest.setJobId(jobId);
        reportedJobDTORequest.setImage(image);

        ReportedJobDTOResponse response = reportedJobService.create(reportedJobDTORequest);

        return ResponseObject.<ReportedJobDTOResponse>builder()
                .message("Reported job created successfully")
                .status(HttpStatus.CREATED.value())
                .data(response)
                .build();
    }


    @PutMapping("/{id}")
    public ResponseObject<ReportedJobDTOResponse> update(@PathVariable Long id, @RequestBody ReportedJobDTORequest request) {
        ReportedJobDTOResponse response = reportedJobService.update(id, request);
        return ResponseObject.<ReportedJobDTOResponse>builder()
                .message("Reported job updated successfully")
                .status(HttpStatus.OK.value())
                .data(response)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseObject<ReportedJobDTOResponse> getById(@PathVariable Long id) {
        return reportedJobService.getById(id)
                .map(response -> ResponseObject.<ReportedJobDTOResponse>builder()
                        .message("Reported job found")
                        .status(HttpStatus.OK.value())
                        .data(response)
                        .build())
                .orElseGet(() -> ResponseObject.<ReportedJobDTOResponse>builder()
                        .message("Reported job not found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .data(null)
                        .build());
    }

    @GetMapping
    public ResponseObject<List<ReportedJobDTOResponse>> getAll() {
        List<ReportedJobDTOResponse> response = reportedJobService.getAll();
        return ResponseObject.<List<ReportedJobDTOResponse>>builder()
                .message("Reported jobs retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(response)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> delete(@PathVariable Long id) {
        return reportedJobService.deleteById(id) ?
                ResponseObject.<Void>builder()
                        .message("Reported job deleted successfully")
                        .status(HttpStatus.NO_CONTENT.value())
                        .data(null)
                        .build() :
                ResponseObject.<Void>builder()
                        .message("Reported job not found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .data(null)
                        .build();
    }

    @GetMapping("/by-job/{jobId}")
    public ResponseObject<List<ReportedJobDTOResponse>> getReportedJobsByJobId(@PathVariable Long jobId) {
        List<ReportedJobDTOResponse> reportedJobs = reportedJobService.getByJobId(jobId);
        return ResponseObject.<List<ReportedJobDTOResponse>>builder()
                .message("Reported jobs for jobId " + jobId + " retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(reportedJobs)
                .build();
    }

    @GetMapping("/by-freelancer/{freelancerId}")
    public ResponseObject<List<ReportedJobDTOResponse>> getReportedJobsByFreelancerId(@PathVariable Long freelancerId) {
        List<ReportedJobDTOResponse> reportedJobs = reportedJobService.getByFreelancerId(freelancerId);
        return ResponseObject.<List<ReportedJobDTOResponse>>builder()
                .message("Reported jobs for freelancerId " + freelancerId + " retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(reportedJobs)
                .build();
    }
}
