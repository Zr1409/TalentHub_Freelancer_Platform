package com.backend.controller.account.freelancer;

import lombok.RequiredArgsConstructor;
import com.backend.dto.ResponseObject;
import com.backend.dto.request.account.freelancer.ProjectDTORequest;
import com.backend.dto.response.account.freelancer.ProjectDTOResponse;
import com.backend.service.intf.account.freelancer.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("/api/v1/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseObject<ProjectDTOResponse> createProject(@Valid @RequestBody ProjectDTORequest projectDTORequest) {
        ProjectDTOResponse projectDTOResponse = projectService.create(projectDTORequest.toEntity());
        return ResponseObject.<ProjectDTOResponse>builder()
                .message("Project created successfully")
                .status(HttpStatus.CREATED.value())
                .data(projectDTOResponse)
                .build();
    }

    @GetMapping("/{id}")
    public ResponseObject<ProjectDTOResponse> getProjectById(@PathVariable Long id) {
        return projectService.getById(id)
                .map(project -> ResponseObject.<ProjectDTOResponse>builder()
                        .message("Project retrieved successfully")
                        .status(HttpStatus.OK.value())
                        .data(project)
                        .build())
                .orElse(ResponseObject.<ProjectDTOResponse>builder()
                        .message("Project not found")
                        .status(HttpStatus.NOT_FOUND.value())
                        .build());
    }

    @GetMapping
    public ResponseObject<List<ProjectDTOResponse>> getAllProjects() {
        List<ProjectDTOResponse> projects = projectService.getAll();
        return ResponseObject.<List<ProjectDTOResponse>>builder()
                .message("All projects retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(projects)
                .build();
    }

    @GetMapping("/freelancer/{freelancerId}")
    public ResponseObject<List<ProjectDTOResponse>> getProjectsByFreelancerId(@PathVariable Long freelancerId) {
        List<ProjectDTOResponse> projects = projectService.getByFreelancerId(freelancerId);
        return ResponseObject.<List<ProjectDTOResponse>>builder()
                .message("Projects for freelancer retrieved successfully")
                .status(HttpStatus.OK.value())
                .data(projects)
                .build();
    }

    @PutMapping("/{id}")
    public ResponseObject<ProjectDTOResponse> updateProject(@PathVariable Long id, @Valid @RequestBody ProjectDTORequest projectDTORequest) {
        try {
            ProjectDTOResponse projectDTOResponse = projectService.update(id, projectDTORequest);
            return ResponseObject.<ProjectDTOResponse>builder()
                    .message("Project updated successfully")
                    .status(HttpStatus.OK.value())
                    .data(projectDTOResponse)
                    .build();
        } catch (Exception e) {
            return ResponseObject.<ProjectDTOResponse>builder()
                    .message("Project not found or could not be updated: " + e.getMessage())
                    .status(HttpStatus.NOT_FOUND.value())
                    .build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseObject<Void> deleteProject(@PathVariable Long id) {
        boolean isDeleted = projectService.deleteById(id);
        if (isDeleted) {
            return ResponseObject.<Void>builder()
                    .message("Project deleted successfully")
                    .status(HttpStatus.NO_CONTENT.value())
                    .build();
        }
        return ResponseObject.<Void>builder()
                .message("Project not found")
                .status(HttpStatus.NOT_FOUND.value())
                .build();
    }
}