package com.example.proj1.repository;

import com.example.proj1.repository.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByMail(String mail);
    boolean existsByUsername(String username);
    Optional<User> findByUserId(Long id);
    Optional<User> findUserByMail(String mail);
}
