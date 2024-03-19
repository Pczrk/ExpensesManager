package com.example.proj1.service;

import com.example.proj1.exceptions.AuthenticationCoreException;
import com.example.proj1.repository.SessionRepository;
import com.example.proj1.repository.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.time.Instant;
import java.util.Base64;
import java.util.Optional;

import com.example.proj1.repository.entity.Session;

import static org.springframework.http.HttpStatus.*;

@Service
@RequiredArgsConstructor
public class SessionService {

    private final SessionRepository sessionRepository;

    private final SecureRandom secureRandom;
    private final UserService userService;

    private String generateSessionId() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        String sessionId = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes).substring(0,32);
        return sessionRepository.existsById(sessionId) ? generateSessionId():sessionId;
    }

    public void createSession(Long userId, HttpServletResponse response) {
        User user = userService.getUserEntity(userId);

        Session session = sessionRepository.save(Session.builder()
                    .sessionId(generateSessionId())
                    .user(user)
                    .expirationDate(Instant.now().plus(Duration.ofMinutes(30)))
                .build());

        Cookie authCookie = new Cookie("Auth",session.getSessionId());
        authCookie.setPath("/");
        authCookie.setMaxAge(1800);

        response.addCookie(authCookie);
    }

    public void noActiveSessionAssertion(String sessionId){
        if (sessionId == null)
            return;
        Optional<Session> session = sessionRepository.findBySessionId(sessionId);
        if (session.isEmpty() || session.get().getExpirationDate().isBefore(Instant.now()))
            return;

        throw new AuthenticationCoreException(CONFLICT,"Active session detected, can't perform this action");
    }

    public void removeCurrentSession(String sessionId){
        if (sessionId == null)
            throw new AuthenticationCoreException(UNAUTHORIZED,"No user to log out");
        Optional<Session> s = sessionRepository.findById(sessionId);
        if (s.isEmpty() || s.get().getExpirationDate().isBefore(Instant.now())) {
            s.ifPresent(sessionRepository::delete);
            throw new AuthenticationCoreException(UNAUTHORIZED, "No user to log out");
        }
        sessionRepository.delete(s.get());
    }

    public Long validateSession(String sessionId){
        if (sessionId == null)
            throw new AuthenticationCoreException(FORBIDDEN,"User is not logged-in or session expired");

        Optional<Session> s = sessionRepository.findById(sessionId);

        if (s.isEmpty() || s.get().getExpirationDate().isBefore(Instant.now()))
            throw new AuthenticationCoreException(FORBIDDEN,"User is not logged-in or session expired");
        return s.get().getUser().getUserId();
    }
}
