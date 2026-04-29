package com.backend.repository;

import com.backend.entity.child.account.freelancer.FreelancerSkill;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FreelancerSkillRepository extends JpaRepository<FreelancerSkill, Long> {
    List<FreelancerSkill> findByFreelancer_Id(Long freelancerId);
    Optional<FreelancerSkill> findByFreelancer_IdAndSkill_Id(Long freelancerId, Long skillId);
    void deleteByFreelancer_IdAndSkill_Id(Long freelancerId, Long skillId);
    Long countBySkillId(Long skillId);
    List<FreelancerSkill> findBySkill_SkillNameIn(List<String> skillNames);
}