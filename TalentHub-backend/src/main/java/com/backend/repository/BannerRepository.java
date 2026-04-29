package com.backend.repository;

import com.backend.entity.child.Banner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BannerRepository extends JpaRepository<Banner, Long> {
    @Query("SELECT COALESCE(SUM(b.price), 0) FROM Banner b")
    Double getTotalBannerRevenue();

    @Query("""
                SELECT SUM(b.price)
                FROM Banner b
                WHERE FUNCTION('YEAR', b.createdAt) = :year 
                AND FUNCTION('MONTH', b.createdAt) = :month
            """)
    Long countSumBannerRevenue(@Param("year") int year, @Param("month") int month);

}
