package com.contrast.demo.repository;

import com.contrast.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    List<User> findByNameContainingIgnoreCase(String name);
    
    List<User> findByEmailContainingIgnoreCase(String email);
    
    // Intentionally vulnerable query for security testing - SQL Injection vulnerability
    @Query(value = "SELECT * FROM users WHERE name LIKE CONCAT('%', :query, '%') OR email LIKE CONCAT('%', :query, '%')", nativeQuery = true)
    List<User> findByQuery(@Param("query") String query);
}
