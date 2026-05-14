package com.backend.mapper.job;

import com.backend.dto.request.job.SaveJobDTORequest;
import com.backend.dto.response.job.SaveJobDTOResponse;
import com.backend.entity.child.job.FreelancerJob;
import com.backend.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import com.backend.utils.TimeRemainingUtils;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", imports = { TimeRemainingUtils.class })
public interface SaveJobMapper extends BaseMapper<FreelancerJob, SaveJobDTORequest, SaveJobDTOResponse> {
	@Mapping(source = "job.client.id", target = "clientId")
	@Mapping(source = "job.client.user.firstName", target = "firstName")
	@Mapping(source = "job.client.user.lastName", target = "lastName")
	@Mapping(source = "job.id", target = "jobId")
	@Mapping(source = "job.title", target = "title")
	@Mapping(source = "job.category.categoryTitle", target = "jobType")
	@Mapping(source = "job.hourWork", target = "hourWork")
	@Mapping(source = "job.fromPrice", target = "fromPrice")
	@Mapping(source = "job.toPrice", target = "toPrice")
	@Mapping(source = "job.description", target = "description")
	@Mapping(expression = "java(mapSkillNames(freelancerJob))", target = "skillNames")
	@Mapping(source = "job.endDate", target = "endDate")
	@Mapping(source = "job.createdAt", target = "createdAt")
	@Mapping(target = "createdTimeFormatted", expression = "java(TimeRemainingUtils.getRelativeTimeFormatted(freelancerJob.getJob().getCreatedAt()))")
	@Mapping(target = "remainingTimeInHours", expression = "java(TimeRemainingUtils.calculateRemainingTimeInHours(freelancerJob.getJob().getEndDate()))")
	@Mapping(target = "remainingTimeFormatted", expression = "java(TimeRemainingUtils.getFormattedTimeRemaining(freelancerJob.getJob().getEndDate()))")
	SaveJobDTOResponse toResponseDto(FreelancerJob freelancerJob);

	default List<String> mapSkillNames(FreelancerJob freelancerJob) {
		if (freelancerJob == null || freelancerJob.getJob().getJobSkills() == null
				|| freelancerJob.getJob().getJobSkills().isEmpty()) {
			return Collections.emptyList();
		}
		return freelancerJob.getJob().getJobSkills().stream().map(jobSkill -> jobSkill.getSkill().getSkillName())
				.collect(Collectors.toList());
	}
}