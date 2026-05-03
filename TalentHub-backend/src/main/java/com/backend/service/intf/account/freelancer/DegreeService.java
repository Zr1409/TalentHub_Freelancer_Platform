package com.backend.service.intf.account.freelancer;

import com.backend.dto.request.account.freelancer.DegreeDTORequest;
import com.backend.dto.response.account.freelancer.DegreeDTOResponse;
import com.backend.service.BaseService;

public interface DegreeService extends BaseService<DegreeDTORequest, DegreeDTOResponse, Long> {
    public DegreeDTOResponse update(Long id, DegreeDTORequest degreeDTORequest);
}
