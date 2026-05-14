package com.backend.mapper.job;

import com.backend.dto.request.job.ClientReviewDTORequest;
import com.backend.dto.response.job.ClientReviewDTOResponse;
import com.backend.entity.child.account.client.ClientReview;
import com.backend.mapper.BaseMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ClientReviewMapper extends BaseMapper<ClientReview, ClientReviewDTORequest, ClientReviewDTOResponse> {
}
