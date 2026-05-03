package com.backend.service.impl.job;

import lombok.RequiredArgsConstructor;
import com.backend.dto.request.job.JobSkillDTORequest;
import com.backend.dto.response.job.JobSkillDTOResponse;
import com.backend.entity.child.job.Job;
import com.backend.entity.child.job.JobSkill;
import com.backend.entity.child.job.Skill;
import com.backend.repository.JobSkillRepository;
import com.backend.repository.JobRepository;
import com.backend.repository.SkillRepository;
import com.backend.service.intf.job.JobSkillService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JobSkillServiceImpl implements JobSkillService {

    private final JobSkillRepository jobSkillRepository;
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    @Override
    public JobSkillDTOResponse create(JobSkillDTORequest jobSkillDTORequest) {
        Optional<Job> jobOptional = jobRepository.findById(jobSkillDTORequest.getJobId());
        Optional<Skill> skillOptional = skillRepository.findById(jobSkillDTORequest.getSkillId());

        if (jobOptional.isEmpty() || skillOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid Job ID or Skill ID");
        }

        Job job = jobOptional.get();
        Skill skill = skillOptional.get();

        JobSkill jobSkill = new JobSkill();
        jobSkill.setJob(job);
        jobSkill.setSkill(skill);

        jobSkill = jobSkillRepository.save(jobSkill);

        return new JobSkillDTOResponse(jobSkill.getId(), jobSkill.getJob().getTitle(), jobSkill.getSkill().getSkillName());
    }


    @Override
    public Optional<JobSkillDTOResponse> getById(Long id) {
        Optional<JobSkill> jobSkill = jobSkillRepository.findById(id);
        return jobSkill.map(js -> new JobSkillDTOResponse(js.getId(), js.getJob().getTitle(), js.getSkill().getSkillName()));
    }

    @Override
    public List<JobSkillDTOResponse> getAll() {
        List<JobSkill> jobSkills = jobSkillRepository.findAll();
        return jobSkills.stream()
                .map(js -> new JobSkillDTOResponse(js.getId(), js.getJob().getTitle(), js.getSkill().getSkillName()))
                .collect(Collectors.toList());
    }

    @Override
    public Boolean deleteById(Long id) {
        if (jobSkillRepository.existsById(id)) {
            jobSkillRepository.deleteById(id);
            return true;
        }
        return false;
    }
}
