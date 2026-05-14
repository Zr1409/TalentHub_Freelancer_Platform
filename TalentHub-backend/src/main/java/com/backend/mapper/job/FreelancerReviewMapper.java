package com.backend.mapper.job;

import com.backend.dto.request.job.FreelancerReviewDTORequest;
import com.backend.dto.response.job.FreelancerReviewDTOResponse;
import com.backend.entity.child.account.freelancer.FreelancerReview;
import com.backend.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FreelancerReviewMapper extends BaseMapper<FreelancerReview, FreelancerReviewDTORequest, FreelancerReviewDTOResponse> {
}
