package com.backend.repository;

import com.backend.entity.child.job.JobSkill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobSkillRepository extends JpaRepository<JobSkill, Long> {
    Long countBySkillId(Long skillId);
    List<JobSkill> findBySkillId(Long skillId);
    List<JobSkill> findByJobId(Long jobId);
}
