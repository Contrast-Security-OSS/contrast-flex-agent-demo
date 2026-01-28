package com.contrast.demo.repository;

import com.contrast.demo.model.AdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for AdminUser entities - INTENTIONALLY VULNERABLE
 * Contains SQL injection vulnerabilities for demonstration
 */
@Repository
public interface AdminUserRepository extends JpaRepository<AdminUser, Long> {
    
    // Safe method using Spring Data JPA
    Optional<AdminUser> findByUsername(String username);
    
    // VULNERABILITY: SQL Injection - native query without proper parameterization
    @Query(value = "SELECT * FROM admin_users WHERE username = '" + 
                   ":username' AND password = ':password'", nativeQuery = true)
    AdminUser findByUsernameAndPasswordUnsafe(@Param("username") String username, 
                                               @Param("password") String password);
}
