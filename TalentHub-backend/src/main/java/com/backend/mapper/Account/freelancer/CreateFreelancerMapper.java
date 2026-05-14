package com.backend.mapper.Account.freelancer;

import com.backend.dto.request.account.freelancer.CreateFreelancerDTORequest;
import com.backend.dto.response.account.freelancer.CreateFreelancerDTOResponse;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CreateFreelancerMapper extends BaseMapper<Freelancer, CreateFreelancerDTORequest, CreateFreelancerDTOResponse> {

    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "userId", target = "user.id")
    Freelancer toEntity(CreateFreelancerDTORequest createFreelancerDTORequest);

    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "user.id", target = "userId")
    CreateFreelancerDTOResponse toResponseDto(Freelancer freelancer);
}
