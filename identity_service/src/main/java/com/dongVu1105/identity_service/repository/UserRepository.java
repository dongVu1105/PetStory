package com.dongVu1105.identity_service.repository;

import com.dongVu1105.identity_service.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByUsername (String username);
    Optional<User> findById(String id);
}
