package com.backend.repository;

import com.backend.entity.child.payment.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {


    @Query("SELECT t,t.payment.createdAt as createPayment FROM Transactions t WHERE t.payment.account.id = :accountId " +
            "order by t.createdAt desc ")
    List<Transactions> getAllTransactionsByAccountId(@Param("accountId") Long accountId);
}
