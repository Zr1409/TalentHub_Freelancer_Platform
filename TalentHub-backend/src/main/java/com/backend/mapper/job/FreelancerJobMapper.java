package com.backend.mapper.job;

import com.backend.dto.request.job.FreelancerJobDTORequest;
import com.backend.dto.response.account.freelancer.ApplicantResponseDTO;
import com.backend.entity.child.job.FreelancerJob;
import com.backend.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
@Mapper(componentModel = "spring")
public interface FreelancerJobMapper extends BaseMapper<FreelancerJob, FreelancerJobDTORequest, ApplicantResponseDTO> {

    @Mapping(source = "freelancerJob.freelancer.user.firstName", target = "firstName")
    @Mapping(source = "freelancerJob.freelancer.user.lastName", target = "lastName")
    @Mapping(source = "freelancerJob.freelancer.user.account.email", target = "email")
    @Mapping(source = "freelancerJob.freelancer.user.image", target = "image")
    @Mapping(expression = "java(mapPosition(freelancerJob))", target = "position")
    @Mapping(source = "freelancerJob.appliedDate", target = "appliedDate")
    @Mapping(source = "freelancerJob.status", target = "status")
    @Mapping(source = "freelancerJob.freelancer.id", target = "freelancerId")
    @Mapping(source = "freelancerJob.job.id", target = "jobId")
    @Mapping(source = "freelancerJob.job.title", target = "jobTitle")
    @Mapping(source = "freelancerJob.cv.id", target = "cvId")
    @Mapping(expression = "java(mapCvURL(freelancerJob))", target = "cvURL")
    @Mapping(source = "freelancerJob.freelancerReview.rating", target = "rating", defaultValue = "0.0")
    @Mapping(target = "appointmentId", source = "appointmentId")
    @Mapping(source = "freelancerJob.clientReview.rating", target = "clientReviewRating", defaultValue = "0.0")
    @Mapping(source = "freelancerJob.clientReview.note", target = "clientReviewNote")
    ApplicantResponseDTO toResponseDto(FreelancerJob freelancerJob, Long appointmentId);

    default String mapPosition(FreelancerJob freelancerJob) {
        if (freelancerJob == null || freelancerJob.getFreelancer() == null
                || freelancerJob.getFreelancer().getExperiences() == null
                || freelancerJob.getFreelancer().getExperiences().isEmpty()) {
            return null;
        }
        return freelancerJob.getFreelancer().getExperiences().get(0).getPosition();
    }
    default String mapCvURL(FreelancerJob freelancerJob) {
        if (freelancerJob == null || freelancerJob.getCv() == null) {
            return null;
        }
        return freelancerJob.getCv().getUrl();
    }
}
