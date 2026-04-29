package com.backend.repository;

import com.backend.entity.child.account.freelancer.CV;
import com.backend.entity.child.account.freelancer.Freelancer;
import com.backend.entity.child.job.FreelancerJob;
import com.backend.entity.child.job.Job;
import com.backend.enums.StatusFreelancerJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FreelancerJobRepository extends JpaRepository<FreelancerJob, Long> {
    // tim freelancer job theo freelancer va job
    Optional<FreelancerJob> findByFreelancer_IdAndJob_Id(Long freelancerId, Long jobId);
    List<FreelancerJob> findByJobId(Long jobId);
    // dem so luong freelancer job theo freelancer va status
    Long countByFreelancerIdAndStatus(Long freelancerId, StatusFreelancerJob status);

    List<FreelancerJob> getApplicantByJobId(Long jobId);

    @Query("select fj " +
            "from FreelancerJob fj " +
            "left join fetch fj.job j " +
            "left join fetch j.category cg " +
            "left join fetch j.jobSkills js " +
            "left join fetch j.client c " +
            "where fj.isSaved = true and fj.freelancer.id = :freelancerId")
    List<FreelancerJob> getSavedJobs(Long freelancerId);

    @Query("SELECT fj " +
            "FROM FreelancerJob fj " +
            "LEFT JOIN FETCH fj.job j " +
            "LEFT JOIN FETCH j.category cg " +
            "LEFT JOIN FETCH j.jobSkills js " +
            "LEFT JOIN FETCH j.client c " +
            "WHERE fj.status IN (:status, :approvedStatus, :rejectStatus) " +
            "AND fj.freelancer.id = :freelancerId")
    List<FreelancerJob> findFreelancerJobsByStatusAndFreelancerId(
            @Param("freelancerId") Long freelancerId,
            @Param("status") StatusFreelancerJob status,
            @Param("approvedStatus") StatusFreelancerJob approvedStatus,
            @Param("rejectStatus") StatusFreelancerJob rejectStatus
    );


    @Query("select fj " +
            "from FreelancerJob fj " +
            "left join fetch fj.job j " +
            "left join fetch j.category cg " +
            "left join fetch j.jobSkills js " +
            "left join fetch j.client c " +
            "where fj.status = :status and fj.freelancer.id = :freelancerId")
    List<FreelancerJob> getApplyJobs(@Param("freelancerId") Long freelancerId, @Param("status") StatusFreelancerJob status);

    @Query("SELECT COUNT(f) FROM FreelancerJob f WHERE f.job = :job AND f.status = :status")
    Long countByJobAndStatus(Job job, StatusFreelancerJob status);

    @Query("SELECT COUNT(f) FROM FreelancerJob f WHERE f.job = :job AND f.status IN :statuses")
    Long countByJobAndStatuses2(Job job, List<StatusFreelancerJob> statuses);



//  @Query("select fj.id," +
//            "u.firstName," +
//            "u.lastName," +
//            "u.image," +
//            "e.position," +
//            "fj.appliedDate," +
//            "fj.status," +
//            "fr.rating " +
//            "from FreelancerJob fj " +
//            "left join fj.freelancerReview fr " +
//            "left join fj.freelancer f " +
//            "left join f.experiences e " +
//            "left join f.user u " +
//            "left join u.account a " +
//            "where fj.job.id = :jobId ")
//    List<Object[]> getApplicantByJobId(@Param("jobId") Long jobId);

    @Query("SELECT f.cv FROM FreelancerJob f WHERE f.freelancer.id = :freelancerId AND f.job.id = :jobId")
    CV getCVByFreelancer_IdAndJob_Id(@Param("freelancerId") Long freelancerId, @Param("jobId") Long jobId);

    long countByStatus(StatusFreelancerJob status);
    List<FreelancerJob> findByFreelancer_Id(Long freelancerId);

    boolean existsByFreelancerIdAndJobId(Long freelancerId, Long jobId);

    @Query("SELECT j FROM Job j " +
            "LEFT JOIN FETCH j.category " +
            "LEFT JOIN FETCH j.client c " +
            "LEFT JOIN FETCH j.jobSkills js " +
            "LEFT JOIN FETCH js.skill " +
            "WHERE j.client.id = :clientId " +
            "ORDER BY j.createdAt DESC")
    List<Job> findByClientIdOrderByCreatedAtDesc(Long clientId);

    @Query("SELECT j FROM Job j " +
            "LEFT JOIN FETCH j.category " +
            "LEFT JOIN FETCH j.client c " +
            "LEFT JOIN FETCH j.jobSkills js " +
            "LEFT JOIN FETCH js.skill " +
            "ORDER BY j.createdAt DESC")
    List<Job> findAllByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(fj) FROM FreelancerJob fj WHERE fj.status = 'Approved' AND MONTH(fj.appliedDate) = :month AND YEAR(fj.appliedDate) = :year")
    Long countApprovedFreelancerJobsByMonth(@Param("month") int month, @Param("year") int year);

    boolean existsByFreelancerIdAndJobIdAndStatus(Long freelancerId, Long jobId, StatusFreelancerJob status);


    @Query("SELECT COUNT(fj) FROM FreelancerJob fj WHERE fj.freelancer.id = :freelancerId AND fj.status = :status AND fj.clientReview IS NOT NULL")
    Long countByFreelancerIdAndStatusAndHasClientReview(@Param("freelancerId") Long freelancerId, @Param("status") StatusFreelancerJob status);

    Long countViewsByJob(Job job);
    List<FreelancerJob> findAllByJobId(Long jobId);

    @Query("SELECT new map(j.id as id, j.title as title, fj.status as status) " +
            "FROM FreelancerJob fj " +
            "JOIN fj.job j " +
            "WHERE fj.freelancer.id = :freelancerId " +
            "AND j.client.id = :clientId ")
    List<Map<String, Object>> findJobInfoByFreelancerIdAndClientId(
            @Param("freelancerId") Long freelancerId,
            @Param("clientId") Long clientId
    );

    @Query("SELECT DISTINCT fj.freelancer FROM FreelancerJob fj " +
            "JOIN fj.job j " +
            "WHERE j.client.id = :clientId " +
            "AND j.status = com.backend.enums.StatusJob.OPEN")
    List<Freelancer> findFreelancersByClientId(@Param("clientId") Long clientId);


    FreelancerJob findByClientReviewId(Long clientReviewId);
}
