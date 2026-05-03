package com.backend.service.intf.account.freelancer;

import com.backend.dto.request.account.freelancer.CreateFreelancerDTORequest;
import com.backend.dto.request.account.freelancer.FreelancerDTORequest;
import com.backend.dto.request.account.freelancer.UpdateHourlyRateDTORequest;
import com.backend.dto.response.account.freelancer.*;
import com.backend.service.BaseService;

import java.util.List;

public interface FreelancerService extends BaseService<FreelancerDTORequest, FreelancerDTOResponse, Long> {
    CreateFreelancerDTOResponse createProfile(CreateFreelancerDTORequest request);

    UpdateHourlyRateDTOResponse updateHourlyRate(UpdateHourlyRateDTORequest request);

    FreelancerDTOResponse updateCategory(Long freelancerId, Long categoryId);

    public List<FreelancerAdminDTOResponse> getAllByAdmin();

    List<FreelancerDTOResponse> getFreelancersByCategoryId(Long categoryId);
    List<FreelancerWithJobsDTOResponse> getFreelancersByClientId(Long clientId);
}

