package com.backend.repository;

import com.backend.entity.child.account.Account;
import com.backend.entity.child.account.User;
import com.backend.enums.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    Optional<Account> getByEmail(String email);

    boolean existsByEmail(String email);

    Optional<Account> findByEmail(String email);

    @Query("SELECT COUNT(a) FROM Account a WHERE YEAR(a.createdAt) = :year AND MONTH(a.createdAt) = :month AND a.role = :role")
    Long countAccountsByRoleAndMonth(@Param("year") int year, @Param("month") int month, @Param("role") RoleUser role);

    Optional<Account> findByUser(User user);
    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.user")
    List<Account> findAllWithUsers();

    @Query("SELECT a FROM Account a LEFT JOIN FETCH a.user WHERE a.lat IS NOT NULL AND a.lng IS NOT NULL")
    List<Account> findAllWithLocation();
}
