package com.backend.mapper.job;

import com.backend.dto.request.job.DetailJobDTORequest;
import com.backend.dto.response.job.DetailJobDTOResponse;
import com.backend.entity.child.job.Job;
import com.backend.mapper.BaseMapper;
import com.backend.utils.TimeRemainingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
@Mapper(componentModel = "spring", imports = {TimeRemainingUtils.class})
public interface DetailJobMapper extends BaseMapper<Job, DetailJobDTORequest, DetailJobDTOResponse> {

    @Mapping(source = "client.id", target = "clientId")
    @Mapping(source = "client.user.firstName", target = "firstName")
    @Mapping(source = "client.user.lastName", target = "lastName")
    @Mapping(source = "category.categoryTitle", target = "type")
    @Mapping(source = "status", target = "status")
    @Mapping(source = "duration", target = "duration")
    @Mapping(source = "scope", target = "scope")
    @Mapping(source = "jobOpportunity", target = "jobOpportunity")
    @Mapping(expression = "java(mapSkillNames(job))", target = "skillNames")
    @Mapping(source = "endDate", target = "endDate")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(target = "createdTimeFormatted", expression = "java(TimeRemainingUtils.getRelativeTimeFormatted(job.getCreatedAt()))")
    @Mapping(target = "remainingTimeInHours", expression = "java(TimeRemainingUtils.calculateRemainingTimeInHours(job.getEndDate()))")
    @Mapping(target = "remainingTimeFormatted", expression = "java(TimeRemainingUtils.getFormattedTimeRemaining(job.getEndDate()))")
    DetailJobDTOResponse toResponseDto(Job job);

    default List<String> mapSkillNames(Job job) {
        if (job == null || job.getJobSkills() == null || job.getJobSkills().isEmpty()) {
            return Collections.emptyList();
        }
        return job.getJobSkills().stream()
                .map(jobSkill -> jobSkill.getSkill().getSkillName())
                .collect(Collectors.toList());
    }
}