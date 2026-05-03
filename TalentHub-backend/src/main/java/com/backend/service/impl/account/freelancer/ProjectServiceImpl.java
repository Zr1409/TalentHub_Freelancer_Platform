package com.backend.service.impl.account.freelancer;

import lombok.RequiredArgsConstructor;
import com.backend.dto.request.account.freelancer.ProjectDTORequest;
import com.backend.dto.response.account.freelancer.ProjectDTOResponse;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.entity.child.account.freelancer.Project;
import com.backend.repository.ProjectRepository;
import com.backend.service.intf.account.freelancer.ProjectService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;

    @Override
    public ProjectDTOResponse create(Project project) {
        Project savedProject = projectRepository.save(project);
        return mapToProjectDTOResponse(savedProject);
    }

    @Override
    public Optional<ProjectDTOResponse> getById(Long id) {
        return projectRepository.findById(id)
                .map(this::mapToProjectDTOResponse);
    }

    @Override
    public List<ProjectDTOResponse> getAll() {
        return projectRepository.findAll().stream()
                .map(this::mapToProjectDTOResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ProjectDTOResponse> getByFreelancerId(Long freelancerId) {
        return projectRepository.findByFreelancerId(freelancerId).stream()
                .map(this::mapToProjectDTOResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectDTOResponse update(Long id, ProjectDTORequest projectDTORequest) {
        Project project = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        project.setTitle(projectDTORequest.getTitle());
        project.setTech(projectDTORequest.getTech());
        project.setDescription(projectDTORequest.getDescription());
        project.setLink(projectDTORequest.getLink());
        project.setImage(projectDTORequest.getImage());

        if (projectDTORequest.getFreelancerId() != null) {
            Freelancer freelancer = new Freelancer();
            freelancer.setId(projectDTORequest.getFreelancerId());
            project.setFreelancer(freelancer);
        }

        Project updatedProject = projectRepository.save(project);
        return mapToProjectDTOResponse(updatedProject);
    }

    @Override
    public Boolean deleteById(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private ProjectDTOResponse mapToProjectDTOResponse(Project project) {
        return ProjectDTOResponse.builder()
                .id(project.getId())
                .title(project.getTitle())
                .tech(project.getTech())
                .description(project.getDescription())
                .link(project.getLink())
                .image(project.getImage())
                .freelancerId(project.getFreelancer() != null ? project.getFreelancer().getId() : null)
                .build();
    }

}