package com.backend.controller.job.jobSkill;

import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.job.JobSkillDTORequest;
import com.backend.dto.response.job.JobSkillDTOResponse;
import com.backend.service.intf.job.JobSkillService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/jobSkills")
@RequiredArgsConstructor
public class JobSkillController {

    private final JobSkillService jobSkillService;

    @PostMapping("")
    public ResponseObject<JobSkillDTOResponse> createJobSkill(@RequestBody JobSkillDTORequest jobSkillDTORequest) {
        JobSkillDTOResponse jobSkillDTOResponse = jobSkillService.create(jobSkillDTORequest);
        return ResponseObject.<JobSkillDTOResponse>builder()
                .message("Job Skill created successfully")
                .status(HttpStatus.CREATED.value())
                .data(jobSkillDTOResponse)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseObject<JobSkillDTOResponse> getJobSkillById(@PathVariable Long id) {
        Optional<JobSkillDTOResponse> jobSkillDTOResponse = jobSkillService.getById(id);
        return jobSkillDTOResponse.map(response ->
                        ResponseObject.<JobSkillDTOResponse>builder()
                                .message("Job Skill found successfully")
                                .status(HttpStatus.OK.value())
                                .data(response)
                                .build())
                .orElse(ResponseObject.<JobSkillDTOResponse>builder()
                        .message("Job Skill not found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @GetMapping("")
    public ResponseObject<List<JobSkillDTOResponse>> getAllJobSkills() {
        List<JobSkillDTOResponse> jobSkillDTOResponses = jobSkillService.getAll();
        return ResponseObject.<List<JobSkillDTOResponse>>builder()
                .message("Job Skills retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(jobSkillDTOResponses)
                .build();
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Boolean> deleteJobSkill(@PathVariable Long id) {
        Boolean isDeleted = jobSkillService.deleteById(id);
        return ResponseObject.<Boolean>builder()
                .message(isDeleted ? "Job Skill deleted successfully" : "Job Skill not found")
                .status(isDeleted ? HttpStatus.OK.value() : HttpStatus.NOT_FOUND.value())
                .data(isDeleted)
                .build();
    }
}
