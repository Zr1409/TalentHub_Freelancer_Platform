package com.backend.repository;

import com.backend.entity.child.account.client.SoldPackage;
import com.backend.enums.TypePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface SoldPackageRepository extends JpaRepository<SoldPackage, Long> {
    List<SoldPackage> findByEndDateBeforeAndStatusAndVoucherPackage_TypePackageIn(
            LocalDateTime endDate, boolean status, List<TypePackage> typePackages);

    SoldPackage findTopByClientIdAndStatusOrderByStartDateDesc(Long clientId, boolean status);


    @Query("SELECT sp FROM SoldPackage sp " +
            "WHERE sp.voucherPackage.typePackage = :type " +
            "AND sp.status = true " +
            "ORDER BY sp.startDate DESC")
    List<SoldPackage> findByVoucherPackageType(TypePackage type);


    @Query("SELECT sp FROM SoldPackage sp " +
            "WHERE sp.client.id = :clientId " +
            "ORDER BY sp.startDate DESC")
    List<SoldPackage> findPackageHistoryByClientId(@Param("clientId") Long clientId);

    // COALESCE giúp tránh trường hợp data null, thay = 0
    @Query("SELECT COALESCE(SUM(s.price), 0) FROM SoldPackage s")
    Double getTotalSoldPackageRevenue();

    @Query("""
                SELECT SUM(sp.price)
                FROM SoldPackage sp
                WHERE FUNCTION('YEAR', sp.startDate) = :year 
                AND FUNCTION('MONTH', sp.startDate) = :month
            """)
    Long countSumSoldPackageRevenue(@Param("year") int year, @Param("month") int month);

}
