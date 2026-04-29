package com.backend.repository;

import com.backend.entity.child.account.client.Client;
import com.backend.enums.StatusAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

public interface ClientRepository extends JpaRepository<Client, Long> {
    public Optional<Client> findByUserId(Long userId);

    @Transactional
    @Modifying
    @Query("update Client c " +
            "set c.fromPrice = :fromPrice, " +
            "c.toPrice = :toPrice, " +
            "c.typePrice = :typePrice " +
            "where c.id = :clientId")
    void updatePrice(@Param("clientId") Long clientId,
                     @Param("fromPrice")  Double fromPrice,
                     @Param("toPrice")  Double toPrice,
                     @Param("typePrice") String typePrice
                     );

    List<Client> findByUser_Account_StatusNot(StatusAccount status);


    @Query("SELECT DISTINCT c " +
            "FROM Client c " +
            "INNER JOIN SoldPackage sp ON sp.client = c " +
            "INNER JOIN sp.voucherPackage vp " +
            "INNER JOIN Job j ON j.client = c " +
            "INNER JOIN j.category cat " +
            "WHERE vp.typePackage = com.backend.enums.TypePackage.DIAMOND " +
            "AND cat.id = :categoryId " +
            "AND sp.status = true")
    List<Client> findClientsByDiamondPackageAndCategoryId(@Param("categoryId") Long categoryId);

}
