package com.backend.mapper.job;

import com.backend.dto.request.job.CreateJobDTORequest;
import com.backend.dto.response.job.CreateJobDTOResponse;
import com.backend.entity.child.job.Job;
import com.backend.entity.child.job.JobSkill;
import com.backend.entity.child.job.Skill;
import com.backend.mapper.BaseMapper;
import com.backend.utils.TimeRemainingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring", imports = {TimeRemainingUtils.class})
public interface CreateJobMapper extends BaseMapper<Job, CreateJobDTORequest, CreateJobDTOResponse> {

    @Mapping(source = "clientId", target = "client.id")
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(target = "jobSkills", expression = "java(mapJobSkills(request.getSkillId()))")
    @Mapping(source = "statusJob", target = "status")
    Job toEntity(CreateJobDTORequest request);
    @Mapping(source = "id", target = "jobId")
    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(target = "skillId", expression = "java(mapSkillIds(job.getJobSkills()))")
    @Mapping(source = "status", target = "statusJob")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(target = "createdTimeFormatted", expression = "java(TimeRemainingUtils.getRelativeTimeFormatted(job.getCreatedAt()))")
    CreateJobDTOResponse toResponseDto(Job job);

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