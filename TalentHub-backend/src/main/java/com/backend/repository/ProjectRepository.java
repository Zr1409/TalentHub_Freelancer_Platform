package com.backend.repository;

import com.backend.entity.child.account.freelancer.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByFreelancerId(Long freelancerId);
}