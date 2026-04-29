package com.backend.repository;

import com.backend.entity.child.account.freelancer.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long> {
    List<Education> findByFreelancerId(Long freelancerId);
    Long countByDegreeId(Long degreeId);
    Long countBySchoolId(Long schoolId);
    List<Education> findBySchoolId(Long schoolId);
}
