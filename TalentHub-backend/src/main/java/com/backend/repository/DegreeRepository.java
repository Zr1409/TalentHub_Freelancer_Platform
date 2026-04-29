package com.backend.repository;

import com.backend.entity.child.account.freelancer.Degree;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DegreeRepository extends JpaRepository<Degree, Long> {
}
