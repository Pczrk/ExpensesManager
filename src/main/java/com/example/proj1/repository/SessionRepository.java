package com.example.proj1.repository;

import com.example.proj1.repository.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, String> {
    boolean existsBySessionId(String sessionId);
    Optional<Session> findBySessionId(String sessionId);
}