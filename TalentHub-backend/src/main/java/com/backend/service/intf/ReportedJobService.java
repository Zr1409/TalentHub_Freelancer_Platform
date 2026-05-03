package com.backend.service.intf;

import com.backend.dto.request.ReportedJobDTORequest;
import com.backend.dto.response.ReportedJobDTOResponse;
import com.backend.service.BaseService;

import java.util.List;

public interface ReportedJobService extends BaseService<ReportedJobDTORequest, ReportedJobDTOResponse, Long> {
    ReportedJobDTOResponse update(Long id, ReportedJobDTORequest request);

    List<ReportedJobDTOResponse> getByJobId(Long jobId);

    List<ReportedJobDTOResponse> getByFreelancerId(Long freelancerId);
}
