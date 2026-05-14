package com.backend.mapper.Account.freelancer;

import com.backend.dto.request.account.freelancer.UpdateHourlyRateDTORequest;
import com.backend.dto.response.account.freelancer.UpdateHourlyRateDTOResponse;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UpdateHourlyRateMapper extends BaseMapper<Freelancer, UpdateHourlyRateDTORequest, UpdateHourlyRateDTOResponse> {

    @Mapping(source = "id", target = "freelancerId")
    UpdateHourlyRateDTOResponse toResponseDto(Freelancer entity);

    @Mapping(source = "freelancerId", target = "id")
    Freelancer toEntity(UpdateHourlyRateDTOResponse dto);

}
