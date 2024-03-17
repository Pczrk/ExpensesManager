package com.example.proj1.repository;

import com.example.proj1.repository.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SessionRepository extends JpaRepository<Session, String> {
    boolean existsBySessionId(String sessionId);
    Optional<Session> findBySessionId(String sessionId);
}