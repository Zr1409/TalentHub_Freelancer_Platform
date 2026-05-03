package com.backend.service.impl.job;

import lombok.RequiredArgsConstructor;
import com.backend.dto.request.job.SkillDTORequest;
import com.backend.dto.response.job.SkillDTOResponse;
import com.backend.entity.child.account.freelancer.FreelancerSkill;
import com.backend.entity.child.job.JobSkill;
import com.backend.entity.child.job.Skill;
import com.backend.exception.BadRequestException;
import com.backend.exception.NotFoundException;
import com.backend.mapper.job.SkillMapper;
import com.backend.repository.FreelancerSkillRepository;
import com.backend.repository.JobSkillRepository;
import com.backend.repository.SkillRepository;
import com.backend.service.intf.job.SkillService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SkillServiceImpl implements SkillService {
    private final SkillMapper skillMapper;
    private final SkillRepository skillRepository;
    private final FreelancerSkillRepository freelancerSkillRepository;
    private final JobSkillRepository jobSkillRepository;
    @Override
    public SkillDTOResponse create(SkillDTORequest skillDTORequest) {
        if (skillDTORequest == null) {
            throw new BadRequestException("Skill request cannot be null");
        }

        if (skillDTORequest.getSkillName() == null || skillDTORequest.getSkillName().isEmpty()) {
            throw new BadRequestException("Skill name cannot be null or empty");
        }

        Skill skill = skillMapper.toEntity(skillDTORequest);
        return skillMapper.toResponseDto(skillRepository.save(skill));
    }

    @Override
    public Optional<SkillDTOResponse> getById(Long id) {
        return skillRepository.findById(id)
                .map(skill -> {
                    Long quantityFreelancerSkill = freelancerSkillRepository.countBySkillId(skill.getId());
                    Long quantityJobSkill = jobSkillRepository.countBySkillId(skill.getId());

                    return new SkillDTOResponse(
                            skill.getId(),
                            skill.getSkillName(),
                            quantityFreelancerSkill,
                            quantityJobSkill
                    );
                });
    }

    @Override
    public List<SkillDTOResponse> getAll() {
        return skillRepository.findAll().stream()
                .map(skill -> {
                    Long quantityFreelancerSkill = freelancerSkillRepository.countBySkillId(skill.getId());
                    Long quantityJobSkill = jobSkillRepository.countBySkillId(skill.getId());

                    return new SkillDTOResponse(
                            skill.getId(),
                            skill.getSkillName(),
                            quantityFreelancerSkill,
                            quantityJobSkill
                    );
                })
                .collect(Collectors.toList());
    }
    @Override
    public Boolean deleteById(Long id) {
        Skill skill = skillRepository.findById(id).orElse(null);

        if (skill == null) {
            return false;
        }

        for (FreelancerSkill freelancerSkill : skill.getFreelancerSkills()) {
            freelancerSkillRepository.delete(freelancerSkill);
        }

        List<JobSkill> listJobSkills = jobSkillRepository.findBySkillId(skill.getId());
        for (JobSkill jobSkill : listJobSkills) {
            jobSkillRepository.delete(jobSkill);
        }

        skillRepository.deleteById(id);
        return true;
    }



    @Override
    public SkillDTOResponse update(Long id, SkillDTORequest skillDTORequest) {
        if (skillDTORequest == null) {
            throw new BadRequestException("Skill request cannot be null");
        }

        if (skillDTORequest.getSkillName() == null || skillDTORequest.getSkillName().isEmpty()) {
            throw new BadRequestException("Skill name cannot be null or empty");
        }

        Skill skill = skillRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Skill not found with id: " + id));

        skill.setSkillName(skillDTORequest.getSkillName());
        return skillMapper.toResponseDto(skillRepository.save(skill));
    }
}