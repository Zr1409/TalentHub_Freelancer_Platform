package com.backend.controller.job.skill;

import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.job.SkillDTORequest;
import com.backend.dto.response.job.SkillDTOResponse;
import com.backend.service.intf.job.SkillService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/jobs/skills")
@RequiredArgsConstructor
public class SkillController {
    private final SkillService skillService;

    @PostMapping("")
    public ResponseEntity<ResponseObject<SkillDTOResponse>> createSkill(@RequestBody SkillDTORequest skillDTORequest) {
        SkillDTOResponse skillDTOResponse = skillService.create(skillDTORequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ResponseObject.<SkillDTOResponse>builder()
                .message("Successfully created skill")
                .status(HttpStatus.CREATED.value())
                .data(skillDTOResponse)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObject<SkillDTOResponse>> getSkillById(@PathVariable Long id) {
        return skillService.getById(id)
                .map(skill -> ResponseEntity.ok(ResponseObject.<SkillDTOResponse>builder()
                        .message("Successfully retrieved skill")
                        .status(HttpStatus.OK.value())
                        .data(skill)
                        .build()))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(ResponseObject.<SkillDTOResponse>builder()
                                .message("Skill not found")
                                .status(HttpStatus.NOT_FOUND.value())
                                .build()));
    }

    @GetMapping("")
    public ResponseEntity<ResponseObject<List<SkillDTOResponse>>> getAllSkills() {
        List<SkillDTOResponse> skills = skillService.getAll();

        return ResponseEntity.ok(ResponseObject.<List<SkillDTOResponse>>builder()
                .message("Successfully retrieved all skills")
                .status(HttpStatus.OK.value())
                .data(skills)
                .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseObject<SkillDTOResponse>> updateSkill(
            @PathVariable Long id,
            @RequestBody SkillDTORequest skillDTORequest) {
        SkillDTOResponse updatedSkill = skillService.update(id, skillDTORequest);

        return ResponseEntity.ok(ResponseObject.<SkillDTOResponse>builder()
                .message("Successfully updated skill")
                .status(HttpStatus.OK.value())
                .data(updatedSkill)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseObject<Void>> deleteSkill(@PathVariable Long id) {
        Boolean isDeleted = skillService.deleteById(id);

        if (isDeleted) {
            return ResponseEntity.ok(ResponseObject.<Void>builder()
                    .message("Successfully deleted skill")
                    .status(HttpStatus.OK.value())
                    .build());
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ResponseObject.<Void>builder()
                        .message("Skill not found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }
}