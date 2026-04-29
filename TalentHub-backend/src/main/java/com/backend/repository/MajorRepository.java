package com.backend.repository;

import com.backend.entity.child.account.freelancer.Major;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MajorRepository extends JpaRepository<Major, Long> {
}
