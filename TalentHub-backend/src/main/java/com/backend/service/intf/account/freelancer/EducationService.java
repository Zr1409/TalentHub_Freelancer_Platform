package com.backend.service.intf.account.freelancer;

import com.backend.dto.request.account.freelancer.EducationDTORequest;
import com.backend.dto.response.account.freelancer.EducationDTOResponse;
import com.backend.service.BaseService;

import java.util.List;

public interface EducationService extends BaseService<EducationDTORequest, EducationDTOResponse, Long> {
    EducationDTOResponse update(Long id, EducationDTORequest educationDTORequest);

    List<EducationDTOResponse> getByFreelancerId(Long freelancerId);
}
