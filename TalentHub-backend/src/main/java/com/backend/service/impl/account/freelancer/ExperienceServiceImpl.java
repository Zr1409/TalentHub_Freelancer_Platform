package com.backend.service.impl.account.freelancer;

import lombok.RequiredArgsConstructor;
import com.backend.dto.request.account.freelancer.ExperienceDTORequest;
import com.backend.dto.response.account.freelancer.ExperienceDTOResponse;
import com.backend.entity.child.account.freelancer.Experience;
import com.backend.exception.BadRequestException;
import com.backend.mapper.Account.freelancer.ExperienceMapper;
import com.backend.repository.ExperienceRepository;
import com.backend.repository.FreelancerRepository;
import com.backend.service.intf.account.freelancer.ExperienceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ExperienceServiceImpl implements ExperienceService {
    private static final Logger log = LoggerFactory.getLogger(ExperienceServiceImpl.class);
    private final ExperienceMapper experienceMapper;
    private final ExperienceRepository experienceRepository;
    private final FreelancerRepository freelancerRepository;

    @Override
    public ExperienceDTOResponse create(ExperienceDTORequest experienceDTORequest) {
        if (experienceDTORequest == null) {
            throw new BadRequestException("ExperienceDTORequest cannot be null");
        }

        if (experienceDTORequest.getCompanyName() == null || experienceDTORequest.getCompanyName().isEmpty()) {
            throw new BadRequestException("CompanyName cannot be null or empty");
        }

        if (experienceDTORequest.getPosition() == null || experienceDTORequest.getPosition().isEmpty()) {
            throw new BadRequestException("Position cannot be null or empty");
        }

        if (experienceDTORequest.getStartDate() == null ) {
            throw new BadRequestException("StartDate cannot be null or empty");
        }

        if (experienceDTORequest.getStatus() == null || experienceDTORequest.getStatus().isEmpty()) {
            throw new BadRequestException("Status cannot be null or empty");
        }

        if (experienceDTORequest.getFreelancerId() == null) {
            throw new BadRequestException("FreelancerId cannot be null");
        }

        Experience experience = experienceMapper.toEntity(experienceDTORequest, freelancerRepository);
        experience = experienceRepository.save(experience);

        return experienceMapper.toResponseDto(experience);
    }

    @Override
    public Optional<ExperienceDTOResponse> getById(Long aLong) {
        if (aLong == null) {
            throw new BadRequestException("Id cannot be null");
        }

        Optional<Experience> experience = experienceRepository.findById(aLong);

        if (experience.isEmpty()) {
            throw new BadRequestException("Experience not found with id: " + aLong);
        }

        return experience.map(experienceMapper::toResponseDto);
    }

    @Override
    public List<ExperienceDTOResponse> getAll() {
        List<Experience> experiences = experienceRepository.findAll();

        return experiences.stream()
                .map(experienceMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public Boolean deleteById(Long aLong) {
        if (aLong == null) {
            throw new BadRequestException("Id cannot be null");
        }

        Optional<Experience> experience = experienceRepository.findById(aLong);

        if (experience.isPresent()) {
            experienceRepository.delete(experience.get());
            return true;
        }

        return false;
    }

    @Override
    public List<ExperienceDTOResponse> getAllByFreelancerId(Long freelancerId) {
        List<Experience> list = experienceRepository.findExperienceByFreelancer_Id(freelancerId);

        return list.stream()
                .map(experience -> {
                    ExperienceDTOResponse dto = experienceMapper.toResponseDto(experience);
                    dto.setId(experience.getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public ExperienceDTOResponse update(Long id, ExperienceDTORequest experienceDTORequest) {
        if (id == null) {
            throw new BadRequestException("Id cannot be null");
        }
        Optional<Experience> existingExperience = experienceRepository.findById(id);
        if (existingExperience.isEmpty()) {
            throw new BadRequestException("Experience not found with id: " + id);
        }
        Experience experience = existingExperience.get();
        if (experienceDTORequest.getCompanyName() != null && !experienceDTORequest.getCompanyName().isEmpty()) {
            experience.setCompanyName(experienceDTORequest.getCompanyName());
        }
        if (experienceDTORequest.getPosition() != null && !experienceDTORequest.getPosition().isEmpty()) {
            experience.setPosition(experienceDTORequest.getPosition());
        }
        if (experienceDTORequest.getStartDate() != null) {
            experience.setStartDate(experienceDTORequest.getStartDate());
        }
        experience.setEndDate(experienceDTORequest.getEndDate());

        if (experienceDTORequest.getDescription() != null) {
            experience.setDescription(experienceDTORequest.getDescription());
        }
        if (experienceDTORequest.getStatus() != null && !experienceDTORequest.getStatus().isEmpty()) {
            experience.setStatus(experienceDTORequest.getStatus());
        }
        if (experienceDTORequest.getFreelancerId() != null) {
            freelancerRepository.findById(experienceDTORequest.getFreelancerId())
                    .ifPresent(experience::setFreelancer);
        }

        experience = experienceRepository.save(experience);

        return experienceMapper.toResponseDto(experience);
    }
}