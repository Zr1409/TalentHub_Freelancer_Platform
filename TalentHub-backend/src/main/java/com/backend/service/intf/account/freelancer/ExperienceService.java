package com.backend.service.intf.account.freelancer;

import com.backend.dto.request.account.freelancer.ExperienceDTORequest;
import com.backend.dto.response.account.freelancer.ExperienceDTOResponse;
import com.backend.service.BaseService;

import java.util.List;

public interface ExperienceService extends BaseService<ExperienceDTORequest, ExperienceDTOResponse, Long> {
    List<ExperienceDTOResponse> getAllByFreelancerId(Long freelancerId);

    ExperienceDTOResponse update(Long id, ExperienceDTORequest experienceDTORequest);
}
