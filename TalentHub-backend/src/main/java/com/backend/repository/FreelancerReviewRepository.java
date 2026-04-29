package com.backend.repository;

import com.backend.entity.child.account.freelancer.FreelancerReview;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FreelancerReviewRepository extends JpaRepository<FreelancerReview, Long> {
}
