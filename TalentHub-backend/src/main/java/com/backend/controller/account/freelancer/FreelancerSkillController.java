package com.backend.controller.account.freelancer;


import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.account.freelancer.FreelancerSkillDTORequest;
import com.backend.dto.response.account.freelancer.FreelancerSkillDTOResponse;
import com.backend.service.intf.account.freelancer.FreelancerSkillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/freelancer-skills")
@RequiredArgsConstructor
public class FreelancerSkillController {
    private final FreelancerSkillService freelancerSkillService;

    @GetMapping
    public ResponseEntity<ResponseObject<List<FreelancerSkillDTOResponse>>> getAllSkills() {
        List<FreelancerSkillDTOResponse> skills = freelancerSkillService.getAllSkills();
        return ResponseEntity.ok(ResponseObject.<List<FreelancerSkillDTOResponse>>builder()
                .message("Successfully retrieved all freelancer skills")
                .status(HttpStatus.OK.value())
                .data(skills)
                .build());
    }

    @GetMapping("/freelancer/{freelancerId}")
    public ResponseEntity<ResponseObject<List<FreelancerSkillDTOResponse>>> getSkillsByFreelancerId(
            @PathVariable Long freelancerId) {
        List<FreelancerSkillDTOResponse> skills = freelancerSkillService.getSkillsByFreelancerId(freelancerId);
        return ResponseEntity.ok(ResponseObject.<List<FreelancerSkillDTOResponse>>builder()
                .message("Successfully retrieved all skills for freelancer with ID: " + freelancerId)
                .status(HttpStatus.OK.value())
                .data(skills)
                .build());
    }

    @PostMapping
    public ResponseEntity<ResponseObject<FreelancerSkillDTOResponse>> addSkillToFreelancer(
            @RequestBody FreelancerSkillDTORequest request) {
        FreelancerSkillDTOResponse response = freelancerSkillService.addSkillToFreelancer(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseObject.<FreelancerSkillDTOResponse>builder()
                        .message("Successfully added skill to freelancer")
                        .status(HttpStatus.CREATED.value())
                        .data(response)
                        .build());
    }

    @DeleteMapping("/{freelancerId}/{skillId}")
    public ResponseEntity<ResponseObject<Void>> removeSkillFromFreelancer(
            @PathVariable Long freelancerId,
            @PathVariable Long skillId) {
        freelancerSkillService.removeSkillFromFreelancer(freelancerId, skillId);
        return ResponseEntity.ok(ResponseObject.<Void>builder()
                .message("Successfully removed skill from freelancer")
                .status(HttpStatus.OK.value())
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<FreelancerSkillDTOResponse>> updateFreelancerSkill(
            @PathVariable Long id,
            @RequestBody FreelancerSkillDTORequest request) {
        FreelancerSkillDTOResponse response = freelancerSkillService.updateFreelancerSkill(id, request);
        return ResponseEntity.ok(ResponseObject.<FreelancerSkillDTOResponse>builder()
                .message("Successfully updated freelancer skill")
                .status(HttpStatus.OK.value())
                .data(response)
                .build());
    }
}