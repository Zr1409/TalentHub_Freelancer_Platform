package com.backend.mapper.job;

import com.backend.dto.request.job.SkillDTORequest;
import com.backend.dto.response.job.SkillDTOResponse;
import com.backend.entity.child.job.Skill;
import com.backend.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SkillMapper extends BaseMapper<Skill, SkillDTORequest, SkillDTOResponse> {
}
