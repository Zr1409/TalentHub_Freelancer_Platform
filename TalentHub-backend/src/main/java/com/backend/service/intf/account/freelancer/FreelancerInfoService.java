package com.backend.service.intf.account.freelancer;

import com.backend.dto.response.account.freelancer.FreelancerInfoDTOResponse;

import java.util.List;

public interface FreelancerInfoService {
    List<FreelancerInfoDTOResponse> getAllFreelancerInfo();
}
