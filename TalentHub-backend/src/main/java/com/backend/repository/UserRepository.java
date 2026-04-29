package com.backend.repository;

import com.backend.entity.child.account.User;
import com.backend.enums.RoleUser;
import com.backend.enums.StatusAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByAccount_Id(Long id);
    List<User> findByAccount_RoleAndAccount_Status(RoleUser role, StatusAccount status);
}