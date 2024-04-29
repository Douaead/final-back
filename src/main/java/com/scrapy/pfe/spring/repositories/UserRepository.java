package com.scrapy.pfe.spring.repositories;

import com.scrapy.pfe.spring.entities.User;
import com.scrapy.pfe.spring.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByEmail(String email);
    User findByUserRole(UserRole userRole);
}

