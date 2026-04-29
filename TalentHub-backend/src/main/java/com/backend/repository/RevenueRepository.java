package com.backend.repository;

import com.backend.entity.child.account.client.SoldPackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RevenueRepository extends JpaRepository<SoldPackage, Long> {
    @Query(value = "SELECT MONTH(s.start_date) AS month, SUM(s.price) " +
            "FROM sold_packages s " +
            "WHERE YEAR(s.start_date) = :year " +
            "GROUP BY MONTH(s.start_date) " +
            "ORDER BY month ASC",
            nativeQuery = true)
    List<Object[]> getRevenueByMonth(@Param("year") int year);

    @Query("SELECT CEIL(MONTH(s.startDate) / 3.0) AS quarter, SUM(s.price) " +
            "FROM SoldPackage s " +
            "WHERE YEAR(s.startDate) = :year " +
            "GROUP BY CEIL(MONTH(s.startDate) / 3.0) " +
            "ORDER BY CEIL(MONTH(s.startDate) / 3.0) ASC")
    List<Object[]> getRevenueByQuarter(@Param("year") int year);


    @Query("SELECT YEAR(s.startDate) AS year, SUM(s.price) " +
            "FROM SoldPackage s " +
            "GROUP BY YEAR(s.startDate) " +
            "ORDER BY YEAR(s.startDate) ASC")
    List<Object[]> getRevenueByYear();


    @Query("SELECT MONTH(b.startTime) AS month, SUM(b.price) " +
            "FROM Banner b " +
            "WHERE YEAR(b.startTime) = :year " +
            "GROUP BY MONTH(b.startTime) " +
            "ORDER BY MONTH(b.startTime) ASC")
    List<Object[]> getRevenueBannerByMonth(@Param("year") int year);


    @Query("SELECT CEIL(MONTH(b.startTime) / 3.0) AS quarter, SUM(b.price) " +
            "FROM Banner b " +
            "WHERE YEAR(b.startTime) = :year " +
            "GROUP BY CEIL(MONTH(b.startTime) / 3.0) " +
            "ORDER BY CEIL(MONTH(b.startTime) / 3.0) ASC")
    List<Object[]> getRevenueBannerByQuarter(@Param("year") int year);


    @Query("SELECT YEAR(b.startTime) AS year, SUM(b.price) AS totalRevenue " +
            "FROM Banner b " +
            "GROUP BY YEAR(b.startTime) " +
            "ORDER BY YEAR(b.startTime) DESC")
    List<Object[]> getRevenueBannerByYear();

   /* @Query("""
                SELECT FUNCTION('WEEK', b.startTime) AS week, SUM(b.price)
                FROM Banner b 
                WHERE YEAR(b.startTime) = :year AND MONTH(b.startTime) = :month
                GROUP BY FUNCTION('WEEK', b.startTime)
                ORDER BY FUNCTION('WEEK', b.startTime) ASC
            """)
    List<Object[]> getRevenueBannerByWeek(@Param("year") int year, @Param("month") int month);

    @Query("""
                SELECT FUNCTION('WEEK', s.startDate) AS week, SUM(s.price)
                FROM SoldPackage s 
                WHERE YEAR(s.startDate) = :year AND MONTH(s.startDate) = :month
                GROUP BY FUNCTION('WEEK', s.startDate)
                ORDER BY FUNCTION('WEEK', s.startDate) ASC
            """)
    List<Object[]> getRevenueByWeek(@Param("year") int year, @Param("month") int month);*/

}
