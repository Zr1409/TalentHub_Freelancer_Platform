package com.backend.mapper.Account.freelancer;
import com.backend.dto.request.account.freelancer.FreelancerSkillDTORequest;
import com.backend.dto.response.account.freelancer.FreelancerSkillDTOResponse;
import com.backend.entity.child.account.freelancer.FreelancerSkill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FreelancerSkillMapper {
    @Mapping(source = "freelancer.id", target = "freelancerId")
    @Mapping(source = "skill.id", target = "skillId")
    @Mapping(source = "skill.skillName", target = "skillName")
    FreelancerSkillDTOResponse toDTO(FreelancerSkill freelancerSkill);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "freelancer", ignore = true)
    @Mapping(target = "skill", ignore = true)
    FreelancerSkill toEntity(FreelancerSkillDTORequest request);
}