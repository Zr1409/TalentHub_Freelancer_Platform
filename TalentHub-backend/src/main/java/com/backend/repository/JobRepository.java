package com.backend.repository;

import com.backend.entity.child.job.Category;
import com.backend.entity.child.job.Job;
import com.backend.enums.StatusAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.backend.enums.StatusJob;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findByCategory(Category category);

    public Long countByCategoryId(Long categoryId);

    @Query("SELECT j FROM Job j " +
            "LEFT JOIN FETCH j.client c " +
            "LEFT JOIN FETCH c.user " +
            "LEFT JOIN FETCH j.category " +
            "LEFT JOIN FETCH j.jobSkills js " +
            "LEFT JOIN FETCH js.skill " +
            "WHERE j.id = :id")
    Optional<Job> getDetailJobById(@Param("id") Long id);


    @Query("SELECT j FROM Job j " +
            "LEFT JOIN FETCH j.client c " +
            "WHERE c.id = :clientId")
    List<Job> getPostedJobs(@Param("clientId") Long clientId);

    //        long countByStatus(StatusJob status);
    long countByStatusNot(StatusJob status);

    List<Job> findByStatus(StatusJob status);

    @Query("SELECT j FROM Job j " +
            "WHERE j.status = :status " +
            "AND j.client.user.account.status <> :bannedStatus")
    List<Job> findOpenJobsWithNonBannedClients(@Param("status") StatusJob status,
                                               @Param("bannedStatus") StatusAccount bannedStatus);


    List<Job> findByEndDateLessThanEqualAndStatusNot(Date endDate, StatusJob status);

    @Query("SELECT j FROM Job j ORDER BY j.createdAt DESC")
    List<Job> findAllByOrderByCreatedAtDesc();

    /*@Query("SELECT j FROM Job j " +
            "WHERE j.client.id = :clientId " +
            "ORDER BY j.createdAt DESC")
    List<Job> findByClientIdOrderByCreatedAtDesc(Long clientId);*/

    @Query("SELECT j FROM Job j " +
            "WHERE j.client.id = :clientId " +
            "AND j.client.user.account.status <> :bannedStatus " +
            "ORDER BY j.createdAt DESC")
    List<Job> findByClientIdAndStatusNotBannedOrderByCreatedAtDesc(@Param("clientId") Long clientId,
                                                                   @Param("bannedStatus") StatusAccount bannedStatus);


    @Query("SELECT COUNT(j) FROM Job j WHERE j.status != :status AND MONTH(j.createdAt) = :month AND YEAR(j.createdAt) = :year")
    Long countNonDraftJobsByMonth(@Param("month") int month, @Param("year") int year, @Param("status") StatusJob status);

    @Query("SELECT DISTINCT j FROM Job j " +
            "LEFT JOIN j.category c " +
            "WHERE j.status = 'OPEN' AND " +
            "(:categoryId IS NOT NULL AND c.id = :categoryId) " +
            "AND j.client.user.account.status <> :bannedStatus " +
            "ORDER BY j.createdAt DESC")
    List<Job> findRecommendedJobsForFreelancer(@Param("categoryId") Long categoryId, @Param("bannedStatus") StatusAccount bannedStatus);

    List<Job> findByClientId(Long clientId);

}
