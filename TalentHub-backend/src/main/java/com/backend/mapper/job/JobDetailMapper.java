package com.backend.mapper.job;

import com.backend.dto.request.job.JobDetailDTORequest;
import com.backend.dto.response.job.JobDetailDTOResponse;
import com.backend.entity.child.job.Job;
import com.backend.entity.child.job.JobSkill;
import com.backend.entity.child.job.Skill;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface JobDetailMapper {
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(target = "skillId", expression = "java(mapSkillIds(job.getJobSkills()))")
    @Mapping(source = "status", target = "statusJob")
    JobDetailDTOResponse toResponseDto(Job job);

    @Mapping(source = "clientId", target = "client.id")
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(target = "jobSkills", expression = "java(mapJobSkills(jobDetailDTORequest.getSkillId()))")
    @Mapping(source = "statusJob", target = "status")
    Job toJob(JobDetailDTORequest jobDetailDTORequest);

    default List<Long> mapSkillIds(List<JobSkill> jobSkills) {
        return jobSkills == null ? List.of() : jobSkills.stream()
                .map(jobSkill -> jobSkill.getSkill().getId())
                .toList();
    }

    default List<JobSkill> mapJobSkills(List<Long> skillIds) {
        return skillIds == null ? List.of() : skillIds.stream()
                .map(skillId -> {
                    JobSkill jobSkill = new JobSkill();
                    Skill skill = new Skill();
                    skill.setId(skillId);
                    jobSkill.setSkill(skill);
                    return jobSkill;
                })
                .toList();
    }

}
