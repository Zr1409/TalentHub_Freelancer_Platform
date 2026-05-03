package com.backend.service.intf.account.freelancer;

import com.backend.dto.request.account.freelancer.SchoolDTORequest;
import com.backend.dto.response.account.freelancer.SchoolDTOResponse;
import com.backend.service.BaseService;

public interface SchoolService extends BaseService<SchoolDTORequest, SchoolDTOResponse, Long> {
    public SchoolDTOResponse update(Long id, SchoolDTORequest schoolDTORequest);
}
