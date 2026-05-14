package com.backend.mapper.job;

import com.backend.dto.request.job.JobDTORequest;
import com.backend.dto.response.job.JobDTOResponse;
import com.backend.dto.response.job.JobWithPackageDTOResponse;
import com.backend.entity.child.job.Job;
import com.backend.entity.child.job.JobSkill;
import com.backend.enums.TypePackage;
import com.backend.mapper.BaseMapper;
import com.backend.utils.TimeRemainingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
@Mapper(componentModel = "spring", imports = {TimeRemainingUtils.class})
public interface JobMapper extends BaseMapper<Job, JobDTORequest, JobDTOResponse> {
    @Mapping(target = "skillName", expression = "java(mapSkills(job.getJobSkills()))")
    @Mapping(target = "categoryName", source = "category.categoryTitle")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(target = "createdTimeFormatted", expression = "java(TimeRemainingUtils.getRelativeTimeFormatted(job.getCreatedAt()))")
    @Mapping(target = "remainingTimeInHours", expression = "java(TimeRemainingUtils.calculateRemainingTimeInHours(job.getEndDate()))")
    @Mapping(target = "remainingTimeFormatted", expression = "java(TimeRemainingUtils.getFormattedTimeRemaining(job.getEndDate()))")
    JobDTOResponse toResponseDto(Job job);

    @Mapping(target = "skillName", expression = "java(mapSkills(job.getJobSkills()))")
    @Mapping(target = "categoryName", source = "job.category.categoryTitle")
    @Mapping(source = "job.createdAt", target = "createdAt")
    @Mapping(target = "createdTimeFormatted", expression = "java(TimeRemainingUtils.getRelativeTimeFormatted(job.getCreatedAt()))")
    @Mapping(target = "typePackage", source = "typePackage")
    @Mapping(target = "remainingTimeInHours", expression = "java(TimeRemainingUtils.calculateRemainingTimeInHours(job.getEndDate()))")
    @Mapping(target = "remainingTimeFormatted", expression = "java(TimeRemainingUtils.getFormattedTimeRemaining(job.getEndDate()))")
    JobWithPackageDTOResponse toResponseWithPackageDto(Job job, TypePackage typePackage);

    default List<String> mapSkills(List<JobSkill> jobSkills) {
        if (jobSkills == null || jobSkills.isEmpty()) {
            return List.of(); // Return an empty list if there are no skills
        }
        return jobSkills.stream()
                .map(js -> js.getSkill().getSkillName()) // Get skill name from `Skill`
                .toList(); // Collect to a list
    }
}