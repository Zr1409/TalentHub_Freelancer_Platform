package com.backend.service.intf.job;

import com.backend.dto.request.job.SkillDTORequest;
import com.backend.dto.response.job.SkillDTOResponse;
import com.backend.service.BaseService;


public interface SkillService extends BaseService<SkillDTORequest, SkillDTOResponse, Long> {
    SkillDTOResponse update(Long id, SkillDTORequest skillDTORequest);
}
