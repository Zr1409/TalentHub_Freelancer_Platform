package com.backend.repository;

import com.backend.entity.child.account.freelancer.Experience;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExperienceRepository extends JpaRepository<Experience, Long> {
    List<Experience> findExperienceByFreelancer_Id(Long freelancerId);
}
