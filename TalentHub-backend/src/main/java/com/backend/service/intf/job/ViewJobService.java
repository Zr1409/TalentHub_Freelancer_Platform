package com.backend.service.intf.job;

import com.backend.dto.request.job.ViewJobDTORequest;
import com.backend.dto.response.job.ViewJobDTOResponse;

public interface ViewJobService {
    ViewJobDTOResponse viewJob(ViewJobDTORequest viewJobDTORequest);
}
