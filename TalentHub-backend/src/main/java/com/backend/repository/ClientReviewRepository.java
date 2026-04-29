package com.backend.repository;

import com.backend.entity.child.account.client.ClientReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClientReviewRepository extends JpaRepository<ClientReview, Long> {
    @Query("SELECT AVG(c.rating) FROM FreelancerJob fj " +
            "JOIN fj.clientReview c " +
            "WHERE fj.freelancer.id = :freelancerId")
    Float findAverageRating(@Param("freelancerId") Long freelancerId);
    @Query("SELECT cr FROM ClientReview cr " +
            "JOIN FreelancerJob fj ON fj.clientReview = cr " +
            "JOIN Job j ON fj.job = j " +
            "WHERE j.client.id = :clientId")
    List<ClientReview> findByClientId(@Param("clientId") Long clientId);
}
