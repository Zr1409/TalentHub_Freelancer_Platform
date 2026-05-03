package com.backend.service.intf.account.freelancer;

import com.backend.dto.request.account.freelancer.ProjectDTORequest;
import com.backend.dto.response.account.freelancer.ProjectDTOResponse;
import com.backend.entity.child.account.freelancer.Project;
import com.backend.service.BaseService;

import java.util.List;

public interface ProjectService extends BaseService<Project, ProjectDTOResponse, Long> {
    List<ProjectDTOResponse> getByFreelancerId(Long freelancerId);

    ProjectDTOResponse update(Long id, ProjectDTORequest projectDTORequest);
}