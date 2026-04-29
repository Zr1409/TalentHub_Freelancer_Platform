package com.backend.repository;

import com.backend.entity.child.account.client.SoldPackage;
import com.backend.entity.child.admin.VoucherPackage;
import com.backend.enums.TypePackage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface VoucherPackageRepository extends JpaRepository<VoucherPackage, Long> {
    VoucherPackage findTopByTypePackageOrderByIdDesc(TypePackage typePackage);

    /*@Query("SELECT v FROM VoucherPackage v WHERE " +
            "COALESCE(v.updatedAt, v.createdAt) = " +
            "(SELECT MAX(COALESCE(v2.updatedAt, v2.createdAt)) " +
            " FROM VoucherPackage v2 WHERE v2.typePackage = v.typePackage)")
    List<VoucherPackage> findLatestVoucherPackagesByType();*/


    @Query("SELECT v FROM VoucherPackage v WHERE v.createdAt = " +
            "(SELECT MAX(v2.createdAt) FROM VoucherPackage v2 WHERE v2.typePackage = v.typePackage)")
    List<VoucherPackage> findLatestVoucherPackagesByType();


    @Query("""
    SELECT v FROM VoucherPackage v 
    WHERE v.createdAt = (
        SELECT MAX(v2.createdAt) 
        FROM VoucherPackage v2 
        WHERE v2.typePackage = v.typePackage
    )
    ORDER BY 
        CASE v.typePackage
            WHEN com.backend.enums.TypePackage.NORMAL THEN 1
            WHEN com.backend.enums.TypePackage.SILVER THEN 2
            WHEN com.backend.enums.TypePackage.GOLD THEN 3
            WHEN com.backend.enums.TypePackage.DIAMOND THEN 4
            ELSE 5
        END
""")
    List<VoucherPackage> findLatestVoucherPackagesByTypeOrdered();




    @Query("SELECT sp FROM SoldPackage sp " +
            "WHERE sp.voucherPackage.typePackage = :type " +
            "AND sp.status = true " +
            "ORDER BY sp.startDate DESC")
    List<SoldPackage> findByVoucherPackageType(TypePackage type);

    Optional<VoucherPackage> findByName(String name);
}
