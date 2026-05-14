package com.backend.mapper.job;

import com.backend.dto.request.job.PostJobsDTORequest;
import com.backend.dto.response.job.PostJobsDTOResponse;
import com.backend.entity.child.job.Job;
import com.backend.enums.StatusFreelancerJob;
import com.backend.mapper.BaseMapper;
import com.backend.utils.TimeRemainingUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring", imports = {TimeRemainingUtils.class})
public interface PostedJobsMapper extends BaseMapper<Job, PostJobsDTORequest, PostJobsDTOResponse> {

    @Mapping(source = "job.category.categoryTitle", target = "type")
    @Mapping(target = "applicants", expression = "java(countApplicants(job))")
    @Mapping(source = "job.createdAt", target = "postedDate")
    @Mapping(source = "job.createdAt", target = "createdAt")
    @Mapping(target = "createdTimeFormatted", expression = "java(TimeRemainingUtils.getRelativeTimeFormatted(job.getCreatedAt()))")
    @Mapping(source = "job.endDate", target = "endDate")
    @Mapping(target = "remainingTimeInHours", expression = "java(TimeRemainingUtils.calculateRemainingTimeInHours(job.getEndDate()))")
    @Mapping(target = "remainingTimeFormatted", expression = "java(TimeRemainingUtils.getFormattedTimeRemaining(job.getEndDate()))")
    PostJobsDTOResponse toResponseDto(Job job);

    default Long countApplicants(Job job) {
        return job.getFreelancerJobs().stream()
                .filter(freelancerJob ->
                        freelancerJob.getStatus() != null &&
                                freelancerJob.getStatus() != StatusFreelancerJob.Viewed
                )
                .count();
    }


}