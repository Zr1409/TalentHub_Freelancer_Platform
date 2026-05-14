package com.backend.mapper.Account.freelancer;

import com.backend.dto.request.account.freelancer.ExperienceDTORequest;
import com.backend.dto.response.account.freelancer.ExperienceDTOResponse;
import com.backend.entity.child.account.freelancer.Experience;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.exception.BadRequestException;
import com.backend.mapper.BaseMapper;
import com.backend.repository.FreelancerRepository;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {FreelancerRepository.class})
public interface ExperienceMapper extends BaseMapper<Experience, ExperienceDTORequest, ExperienceDTOResponse> {

    @Mapping(target = "freelancer", expression = "java(mapFreelancerIdToFreelancer(dto.getFreelancerId(), freelancerRepository))")
    Experience toEntity(ExperienceDTORequest dto, @Context FreelancerRepository freelancerRepository);

    @Mapping(target = "id", source = "id")
    @Mapping(target = "freelancer", source = "freelancer")
    ExperienceDTOResponse toResponseDto(Experience entity);

    default Freelancer mapFreelancerIdToFreelancer(Long freelancerId, FreelancerRepository freelancerRepository) {
        if (freelancerId == null) {
            throw new BadRequestException("freelancerId cannot be null");
        }

        return freelancerRepository.findById(freelancerId)
                .orElseThrow(() -> new BadRequestException("Freelancer not found with id: " + freelancerId));
    }
}