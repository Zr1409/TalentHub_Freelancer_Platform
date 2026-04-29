package com.backend.repository;

import com.backend.entity.child.ReportedJob;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportedJobRepository extends JpaRepository<ReportedJob, Long> {
    List<ReportedJob> findByJobId(Long jobId);
    List<ReportedJob> findByFreelancerId(Long freelancerId);
}
