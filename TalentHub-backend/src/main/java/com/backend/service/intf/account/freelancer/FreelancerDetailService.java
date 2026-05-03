package com.backend.service.intf.account.freelancer;

import com.backend.dto.response.account.freelancer.FreelancerDetailDTOResponse;

public interface FreelancerDetailService {
    FreelancerDetailDTOResponse getFreelancerDetail(Long freelancerId);
}
